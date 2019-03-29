package asad.model.dataaccess.repository;

import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.Taxonomy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;


public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Optional<Article> findById(Integer id);

    @Query("select a from Article a join fetch a.authors where a.id = :id")
    Article findArticleCompleteInfo(@Param("id") Integer id);

    @Query("select a.taxonomies from Article a inner join a.taxonomies where a.id = :id")
    Set<Taxonomy> findArticleTaxonomies(@Param("id") Integer id);
}