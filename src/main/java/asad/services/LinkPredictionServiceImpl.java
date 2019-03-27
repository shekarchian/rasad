package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.entity.Article;
import asad.model.entity.Author;
import asad.repository.ArticleRepository;
import asad.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkPredictionServiceImpl implements LinkPredictionService {


    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public Author getAuthorInfo(Integer id) {
        return authorRepository.findById(id).get();
    }

    @Override
    public List<Article> getAuthorArticles(Integer id) {
        return authorRepository.findAuthorArticles(id).getArticles();
    }

    @Override
    public Article getArticleInfo(Integer id) {
        return articleRepository.findById(id).get();
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
    public List<String> getAuthorTopicCcs(String code) {
        return null;
    }

    @Override
    public List<String> getArticleTopicCcs(String code) {
        return null;
    }

    @Override
    public List<String> getAuthorTopicKeywords(String code) {
        return null;
    }

    @Override
    public List<String> getArticleTopicKeywords(String code) {
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
