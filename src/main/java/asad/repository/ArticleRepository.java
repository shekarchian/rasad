package asad.repository;

import asad.model.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Optional<Article> findById(Integer id);


}