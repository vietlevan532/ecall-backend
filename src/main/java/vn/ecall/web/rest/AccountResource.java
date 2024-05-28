package vn.ecall.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.ecall.domain.User;
import vn.ecall.repository.UserRepository;
import vn.ecall.security.SecurityUtils;
import vn.ecall.service.EmailAlreadyUsedException;
import vn.ecall.service.InvalidPasswordException;
import vn.ecall.service.MailService;
import vn.ecall.service.UserService;
import vn.ecall.service.dto.AdminUserDTO;
import vn.ecall.service.dto.PasswordChangeDTO;
import vn.ecall.web.rest.vm.KeyAndPasswordVM;
import vn.ecall.web.rest.vm.ManagedUserVM;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) { super(message); }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws vn.ecall.service.LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userService.registerUser(managedUserVM, managedUserVM.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost:3000/login");  // The problem of register successfully to redirect => http://localhost:3000/http://localhost:3000/login
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Added sale account
    //@PostMapping("/sale-register")
    //@ResponseStatus(HttpStatus.CREATED)
    //ResponseEntity<Void> registerSaleAccount(@Valid @RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException {
        //if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            //throw new InvalidPasswordException();
        //}
        //userService.registerSale(managedUserVM, managedUserVM.getPassword());
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Location", "http://localhost:3000/login");  // The problem of register successfully to redirect => http://localhost:3000/http://localhost:3000/login
        //return new ResponseEntity<>(headers, HttpStatus.CREATED);
    //}

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
                .getUserWithAuthorities()
                .map(AdminUserDTO::new)
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * Allow CURRENT user to change the name
     * @param userDTO
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
                userDTO.getFullName()
        );
    }

    /**
     * {@code POST /account/change-password} : changes the current user's password
     * @param passwordChangeDto current and new password
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }


    private static boolean isPasswordLengthInvalid(String password) {
        return (
                StringUtils.isEmpty(password) ||
                        password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
                        password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }


}
