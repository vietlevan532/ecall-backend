package vn.ecall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ecall.domain.businessmodel.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findOneByName(String name);

    Optional<Category> findOneByCategoryId(Integer id);
}
