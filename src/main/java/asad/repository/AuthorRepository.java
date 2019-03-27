package asad.repository;

import asad.model.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Optional<Author> findById(Integer id);

    @Query("select a from Author a join fetch a.articles where a.id = :id")
    Author findAuthorArticles(@Param("id") Integer id);
}