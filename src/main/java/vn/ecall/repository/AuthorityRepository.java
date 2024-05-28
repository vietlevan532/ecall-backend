package vn.ecall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ecall.domain.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
