package asad.model;

public class ArticleAuthor {

    private Integer id;
    private Integer articleId;
    private Integer authorId;

    public ArticleAuthor(Integer id, Integer articleId, Integer authorId) {
        this.id = id;
        this.articleId = articleId;
        this.authorId = authorId;
    }

    public ArticleAuthor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}
