package asad.model.dataaccess.repository;

import asad.model.dataaccess.entity.ArticlesPredictedLink;
import asad.model.dataaccess.entity.AuthorsPredictedLink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticlePredictedLinkRepository extends CrudRepository<ArticlesPredictedLink, Integer> {

    @Query("select link from ArticlesPredictedLink link " +
            "where link.article1 = :id or link.article2 = :id ")
    List<ArticlesPredictedLink> findByArticleId(@Param("id") Integer id);

}
