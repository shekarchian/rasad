package asad.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import asad.model.*;
import asad.services.LinkPredictionService;

import java.util.List;


@RestController
@RequestMapping("/api/link-prediction")
public class LinkPredictionController {

    @Autowired
    private LinkPredictionService linkPredictionService;

    @PostMapping("predicted-links")
    public PredictedLinks predictLinks(@RequestBody PredictedLinksRequest predictedLinksRequest) {
        return linkPredictionService.getPredictedLinks(predictedLinksRequest);
    }

    @GetMapping("author/{code}")
    public Author getAuthorInfo(@PathVariable String code) {
        return linkPredictionService.getAuthorInfo(code);
    }

    @GetMapping("co-authors/{code}")
    public List<Author> getCoAuthors(@PathVariable String code) {
        return linkPredictionService.getCoAuthors(code);
    }

    @GetMapping("predicted-co-authors/{code}")
    public List<Author> getPredictedCoAuthors(@PathVariable String code) {
        return linkPredictionService.getPredictedCoAuthors(code);
    }

    @GetMapping("author-articles/{code}")
    public List<Article> getAuthorArticles(@PathVariable String code) {
        return linkPredictionService.getAuthorArticles(code);
    }

    @GetMapping("article-info/{code}")
    public Article getArticleInfo(@PathVariable String code) {
        return linkPredictionService.getArticleInfo(code);
    }

    @GetMapping("related-articles/{code}")
    public List<Article> getRelatedArticles(@PathVariable String code) {
        return linkPredictionService.getRelatedArticles(code);
    }

    @GetMapping("predicted-related-articles/{code}")
    public List<Article> getPredictedRelatedArticles(@PathVariable String code) {
        return linkPredictionService.getPredictedRelatedArticles(code);
    }

    @GetMapping("author-integrated-topics/{code}")
    public List<String> getAuthorTopics(@PathVariable String code) {
        return linkPredictionService.getAuthorTopic(code);
    }

    @GetMapping("article-integrated-topics/{code}")
    public List<String> getArticleTopics(@PathVariable String code) {
        return linkPredictionService.getArticleTopic(code);
    }

    @GetMapping("author-topic-ccs/{code}")
    public List<String> getAuthorTopicCcs(@PathVariable String code) {
        return linkPredictionService.getAuthorTopicCcs(code);
    }

    @GetMapping("article-topic-ccs/{code}")
    public List<String> getArticleTopicCcs(@PathVariable String code) {
        return linkPredictionService.getArticleTopicCcs(code);
    }

    @GetMapping("author-topics-keyword/{code}")
    public List<String> getAuthorTopicKeywords(@PathVariable String code) {
        return linkPredictionService.getAuthorTopicKeywords(code);
    }

    @GetMapping("article-topics-keywords/{code}")
    public List<String> getArticleTopicKeywords(@PathVariable String code) {
        return linkPredictionService.getArticleTopicKeywords(code);
    }

    @GetMapping("author-topics-probability/{code}")
    public List<TopicProbability> getAuthorTopicProbability(@PathVariable String code) {
        return linkPredictionService.getAuthorTopicProbability(code);
    }

    @GetMapping("article-topics-probability/{code}")
    public List<TopicProbability> getArticleTopicProbability(@PathVariable String code) {
        return linkPredictionService.getArticleTopicProbability(code);
    }

    @GetMapping("author-topics-words/{authorCode}/{topicId}")
    public List<String> getAuthorTopicWords(@PathVariable String authorCode,@PathVariable String topicId) {
        return linkPredictionService.getAuthorTopicWords(authorCode, topicId);
    }

    @GetMapping("article-topics-words/{articleCode}/{topicId}")
    public List<String> getArticleTopicWords(@PathVariable String articleCode,@PathVariable String topicId) {
        return linkPredictionService.getArticleTopicWords(articleCode, topicId);
    }
}