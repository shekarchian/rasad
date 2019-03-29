package asad.model.dataaccess.repository;

import asad.model.dataaccess.entity.ArticleKeyword;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ArticleKeywordRepository extends CrudRepository<ArticleKeyword, Integer> {
    Set<ArticleKeyword> findByArticle_Id(Integer id);
}