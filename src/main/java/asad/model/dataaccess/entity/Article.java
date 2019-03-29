package asad.model.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;


@Entity
public class Article {
    @Id
    private Integer id;
    private String title;
    private String journal;
    private String publisher;
    private Date date;
    private String volume;

    @Column(name = "abstract")
    private String abstractColumn;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_author",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "article_taxonomy_class",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "taxonomy_class_id")})
    private Set<Taxonomy> taxonomies = new HashSet<>();

    public Article() {
    }

    public Article(Integer id, String title, String journal, String publisher, Date date, String volume, String abstractColumn, Set<Author> authors) {
        this.id = id;
        this.title = title;
        this.journal = journal;
        this.publisher = publisher;
        this.date = date;
        this.volume = volume;
        this.abstractColumn = abstractColumn;
        this.authors = authors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAbstractColumn() {
        return abstractColumn;
    }

    public void setAbstractColumn(String abstractColumn) {
        this.abstractColumn = abstractColumn;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Taxonomy> getTaxonomies() {
        return taxonomies;
    }
}