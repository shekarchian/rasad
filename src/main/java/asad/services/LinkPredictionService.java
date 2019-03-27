package asad.services;

import asad.model.*;
import asad.model.entity.Article;
import asad.model.entity.Author;

import java.util.List;

public interface LinkPredictionService {
    Author getAuthorInfo(Integer code);
    List<Article> getAuthorArticles(Integer id);
    Article getArticleInfo(Integer id);
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
