package asad.services;

import asad.model.*;

import java.util.List;

public interface LinkPredictionService {
    PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest);
    Author getAuthorInfo(String code);
    List<Author> getCoAuthors(String code);
    List<Author> getPredictedCoAuthors(String code);

    List<Article> getAuthorArticles(String code);
    Article getArticleInfo(String code);
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

    List<String> getAuthorTopicWords(String authorCode, String topicId);

    List<String> getArticleTopicWords(String articleCode, String topicId);
}
