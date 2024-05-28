package vn.ecall.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ecall.domain.businessmodel.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findOneByName(String name);

    Optional<Product> findOneById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    Page<Product> findAllByCategoryId(@Param(("categoryId")) Integer id, Pageable pageable);

    List<Product> findByNameContaining(String name);

    //@Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    //List<Product> findAllCategoriesByProductId(Integer id);
    List<Product> findAllByCategory_categoryId(Integer categoryId);

}

