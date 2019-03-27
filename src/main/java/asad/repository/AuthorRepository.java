package asad.repository;

import asad.model.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Optional<Author> findById(Integer id);

    @Query("select author from Author author " +
            "inner join fetch author.articles article1 " +
            "inner join fetch article1.authors author2 " +
            "where author.id = :id ")
    Author findAuthorArticles(@Param("id") Integer id);
}