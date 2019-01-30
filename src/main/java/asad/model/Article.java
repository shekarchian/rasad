package asad.model;

import java.util.List;

public class Article {
    String code;
    String name;
    List<Author> authors;

    public Article(String code, String name, List<Author> authors) {
        this.code = code;
        this.name = name;
        this.authors = authors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
