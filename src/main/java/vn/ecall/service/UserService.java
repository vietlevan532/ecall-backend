package vn.ecall.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ecall.domain.Authority;
import vn.ecall.domain.User;
import vn.ecall.repository.AuthorityRepository;
import vn.ecall.repository.UserRepository;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.security.SecurityUtils;
import vn.ecall.service.dto.AdminUserDTO;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public void registerUser(AdminUserDTO userDTO, String password) {
        userRepository
                .findOneByLogin(userDTO.getLogin().toLowerCase())
                .ifPresent(
                        existingUser -> {
                            throw new LoginAlreadyUsedException();
                        }
                );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFullName(userDTO.getFullName());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setGender(userDTO.getGender());
        newUser.setAddress(userDTO.getAddress());
        newUser.setPhone(userDTO.getPhone());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
    }

    public void registerSale(AdminUserDTO userDTO, String password) {
        userRepository
                .findOneByLogin(userDTO.getLogin().toLowerCase())
                .ifPresent(
                        existingUser -> {
                            throw new LoginAlreadyUsedException();
                        }
                );
        User newSale = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newSale.setLogin(userDTO.getLogin().toLowerCase());
        newSale.setPassword(encryptedPassword);
        newSale.setFullName(userDTO.getFullName());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.SALE).ifPresent(authorities::add);
        newSale.setAuthorities(authorities);
        newSale.setGender(userDTO.getGender());
        newSale.setAddress(userDTO.getAddress());
        newSale.setPhone(userDTO.getPhone());
        userRepository.save(newSale);
        log.debug("Created Information for Sale: {}", newSale);
    }

    /**
     * For admin to create user if needed
     * @param userDTO
     * @return
     */
    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFullName(userDTO.getFullName());
        String encryptedPassword = passwordEncoder.encode(RandomStringUtils.random(20, 0, 0, true, true, (char[])null, new SecureRandom()));
        user.setPassword(encryptedPassword);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * For admin to delete user if needed
     * @param login
     */
    public void deleteUser(String login) {
        userRepository
                .findOneByLogin(login)
                .ifPresent(
                        user -> {
                            userRepository.delete(user);
                            log.debug("Deleted User: {}", user);
                        }
                );
    }

    /**
     * Update BASIC information (fullname in this case) for the current user.
     *
     * @param fullName first name of user.
     */
    public void updateUser(String fullName) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(
                        user -> {
                            user.setFullName(fullName);
                            log.debug("Changed Information for User: {}", user);
                        }
                );
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
                .of(userRepository.findOneById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        user -> {
                            user.setLogin(userDTO.getLogin().toLowerCase());
                            user.setFullName(userDTO.getFullName());
                            user.setAddress(userDTO.getAddress());
                            user.setGender(userDTO.getPhone());
                            user.setPhone(userDTO.getPhone());
                            Set<Authority> managedAuthorities = user.getAuthorities();
                            managedAuthorities.clear();
                            userDTO
                                    .getAuthorities()
                                    .stream()
                                    .map(authorityRepository::findById)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .forEach(managedAuthorities::add);
                            log.debug("Changed Information for User: {}", user);
                            return user;
                        }
                )
                .map(AdminUserDTO::new);
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(
                        user -> {
                            String currentEncryptedPassword = user.getPassword();
                            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                                throw new InvalidPasswordException();
                            }
                            String encryptedPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(encryptedPassword);
                            log.debug("Change password for User: {}", user);
                        }
                );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserDTO> getUserById(Integer id) {
        return userRepository.findOneById(id).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

}
