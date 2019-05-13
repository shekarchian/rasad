package asad.model.dataaccess.entity;

import javax.persistence.*;

@Entity
public class AuthorsPredictedLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer author1;

    private Integer author2;
    private Double weight;

    public AuthorsPredictedLink() {
    }

    public AuthorsPredictedLink(Integer author1, Integer author2, Double weight) {
        this.author1 = author1;
        this.author2 = author2;
        this.weight = weight;
    }

    public Integer getAuthor1() {
        return author1;
    }

    public void setAuthor1(Integer author1) {
        this.author1 = author1;
    }

    public Integer getAuthor2() {
        return author2;
    }

    public void setAuthor2(Integer author2) {
        this.author2 = author2;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
