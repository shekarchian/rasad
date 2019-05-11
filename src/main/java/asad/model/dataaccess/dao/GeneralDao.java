/*
package asad.model.dataaccess.dao;

import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.DenormalizedLemmatizedArticleText;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

*/
/**
 * @author Reza Shekarchian
 *//*

@Service
@Transactional
public class GeneralDao {

    public List<Article> getArticlesWithKeyword(){
        TypedQuery<DenormalizedLemmatizedArticleText> query =
                em.createQuery("SELECT dla FROM Author a join DenormalizedLemmatizedArticleText dla " +
                                "on a." +
                        " ArticleKeyword ak on ak.article.id = a.id where a.id = 20489",
                Object[].class);
        List<Object[]> articles = query.getResultList();
        return null;
    }


    @PersistenceContext
    private EntityManager em;
}
*/
