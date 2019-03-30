package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.ArticleKeyword;
import asad.model.dataaccess.entity.Author;
import asad.model.dataaccess.entity.Taxonomy;
import asad.model.dataaccess.repository.ArticleKeywordRepository;
import asad.model.dataaccess.repository.ArticleRepository;
import asad.model.dataaccess.repository.AuthorRepository;
import asad.model.wrapper.ArticleWrapper;
import asad.model.wrapper.AuthorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LinkPredictionServiceImpl implements LinkPredictionService {


    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleKeywordRepository articleKeywordRepository;

    private List<Integer> rootTopicsId = Arrays.asList(new Integer[]{2902,2922,3057,3374,3450,3558,3664,3793,3979,4210,4345,4402,4616});
    public AuthorWrapper getAuthorInfo(Integer id) {
        Author author = authorRepository.findById(id).get();
        return new AuthorWrapper(author);
    }

    @Override
    public Set<ArticleWrapper> getAuthorArticles(Integer id) {
        Set<Article> articles = authorRepository.findAuthorArticles(id).getArticles();
        Set<ArticleWrapper> articleWrappers = new HashSet<>();
        for (Article article : articles) {
            articleWrappers.add(new ArticleWrapper(article));
        }
        return articleWrappers;
    }

    @Override
    public ArticleWrapper getArticleInfo(Integer id) {
        Article article = articleRepository.findArticleCompleteInfo(id);
        return new ArticleWrapper(article);
    }

    @Override
    public Set<String> getArticleTopicKeywords(String code) {
        Set<ArticleKeyword> articleKeywords = articleKeywordRepository.findByArticle_Id(Integer.parseInt(code));
        Set<String> keywords = new HashSet<>();
        articleKeywords.forEach((keyword) -> keywords.add(keyword.getKeyword()));
        return keywords;
    }

    @Override
    public Set<String> getArticleTopicCcs(String code) {
        Set<Taxonomy> articleTaxonomies = articleRepository.findArticleTaxonomies(Integer.parseInt(code));
        Set<String> taxonomies = new HashSet<>();
        articleTaxonomies.forEach((taxonomy) -> {
            if (!rootTopicsId.contains(taxonomy.getId()) && !rootTopicsId.contains(taxonomy.getParent_taxonomy_class_id()))
            taxonomies.add(taxonomy.getTitle());
        });
        if (taxonomies.size() <3) {
            articleTaxonomies.forEach((taxonomy) -> {
                if (rootTopicsId.contains(taxonomy.getParent_taxonomy_class_id()))
                    taxonomies.add(taxonomy.getTitle());
            });
        }
        return taxonomies;
    }

    @Override
    public Set<String> getAuthorTopicKeywords(String code) {
        Set<ArticleKeyword>  articleKeywords = articleKeywordRepository.findAuthorKeywords(Integer.parseInt(code));
        Set<String> keywords = new HashSet<>();
        articleKeywords.forEach((keyword) -> keywords.add(keyword.getKeyword()));
        return keywords;
    }

    @Override
    public Set<String> getAuthorTopicCcs(String code) {
        Set<Taxonomy>  authorTaxonomies = authorRepository.findAuthorTaxonomies(Integer.parseInt(code));
        Set<String> taxonomies= new HashSet<>();
        authorTaxonomies.forEach((taxonomy) -> {
            if (!rootTopicsId.contains(taxonomy.getId()) && !rootTopicsId.contains(taxonomy.getParent_taxonomy_class_id()))
                taxonomies.add(taxonomy.getTitle());
        });
        return taxonomies;
    }

    @Override
    public PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest) {
        return null;
    }

    @Override
    public List<Author> getCoAuthors(String code) {
        return null;
    }

    @Override
    public List<Author> getPredictedCoAuthors(String code) {
        return null;
    }

    @Override
    public List<Article> getRelatedArticles(String code) {
        return null;
    }

    @Override
    public List<Article> getPredictedRelatedArticles(String code) {
        return null;
    }

    @Override
    public List<String> getAuthorTopic(String code) {
        return null;
    }

    @Override
    public List<String> getArticleTopic(String code) {
        return null;
    }

    @Override
    public List<TopicProbability> getAuthorTopicProbability(String code) {
        return null;
    }

    @Override
    public List<TopicProbability> getArticleTopicProbability(String code) {
        return null;
    }
}
