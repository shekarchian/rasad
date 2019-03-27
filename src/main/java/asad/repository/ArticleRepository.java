package asad.repository;

import asad.model.entity.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Optional<Article> findById(Integer id);

    @Query("select a from Article a join fetch a.authors where a.id = :id")
    Article findArticleCompleteInfo(@Param("id") Integer id);

}