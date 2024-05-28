package vn.ecall.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ecall.domain.businessmodel.News;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    Optional<News> findOneById(Integer id);

    @EntityGraph(attributePaths = "newsImage")
    Optional<News> findOneWithNewsImageById(Integer id);
}
