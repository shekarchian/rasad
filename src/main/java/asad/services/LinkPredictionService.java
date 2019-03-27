package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.entity.Article;
import asad.model.entity.Author;
import asad.model.wrapper.ArticleWrapper;
import asad.model.wrapper.AuthorWrapper;

import java.util.List;
import java.util.Set;

public interface LinkPredictionService {
    AuthorWrapper getAuthorInfo(Integer code);
    Set<ArticleWrapper> getAuthorArticles(Integer id);
    ArticleWrapper getArticleInfo(Integer id);
    PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest);

    List<Author> getCoAuthors(String code);
    List<Author> getPredictedCoAuthors(String code);
    List<Article> getRelatedArticles(String code);
    List<Article> getPredictedRelatedArticles(String code);

    List<String> getAuthorTopic(String code);

    List<String> getArticleTopic(String code);

    List<String> getAuthorTopicCcs(String code);

    List<String> getArticleTopicCcs(String code);


    List<String> getAuthorTopicKeywords(String code);

    List<String> getArticleTopicKeywords(String code);

    List<TopicProbability> getAuthorTopicProbability(String code);

    List<TopicProbability> getArticleTopicProbability(String code);


}
