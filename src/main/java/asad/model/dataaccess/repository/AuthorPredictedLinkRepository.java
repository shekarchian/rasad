package asad.model.dataaccess.repository;

import asad.model.dataaccess.entity.AuthorsPredictedLink;
import asad.model.dataaccess.entity.Taxonomy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AuthorPredictedLinkRepository extends CrudRepository<AuthorsPredictedLink, Integer> {

    @Query("select link from AuthorsPredictedLink link " +
            "where link.author1 = :id or link.author2 = :id ")
    List<AuthorsPredictedLink> findByAuthorId(@Param("id") Integer id);

}
