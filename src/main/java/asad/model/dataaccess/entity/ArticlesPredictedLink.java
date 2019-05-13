package asad.model.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArticlesPredictedLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer article1;

    private Integer article2;
    private Double weight;

    public ArticlesPredictedLink() {
    }

    public ArticlesPredictedLink(Integer article1, Integer article2, Double weight) {
        this.article1 = article1;
        this.article2 = article2;
        this.weight = weight;
    }

    public Integer getArticle1() {
        return article1;
    }

    public void setArticle1(Integer article1) {
        this.article1 = article1;
    }

    public Integer getArticle2() {
        return article2;
    }

    public void setArticle2(Integer article2) {
        this.article2 = article2;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
