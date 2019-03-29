package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.Author;
import asad.model.wrapper.ArticleWrapper;
import asad.model.wrapper.AuthorWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LinkPredictionServiceFakeResult implements LinkPredictionService {
    @Override
    public AuthorWrapper getAuthorInfo(Integer code) {

        return null;
    }

    @Override
    public Set<ArticleWrapper> getAuthorArticles(Integer id) {
        return null;
    }

    @Override
    public ArticleWrapper getArticleInfo(Integer id) {
        return null;
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
    public Set<String> getArticleTopicCcs(String code) {
        return null;
    }

    @Override
    public List<String> getAuthorTopicKeywords(String code) {
        return null;
    }

    @Override
    public Set<String> getArticleTopicKeywords(String code) {
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

/*
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public Author getAuthorInfo(Integer id) {
        return authorRepository.findById(id).get();
    }

    public List<Article> getAuthorArticles(Integer id) {




       */
/* Author author1 = new Author(1234, "عباس نوری");
        Author author2 = new Author(1245, "جلیل جلیلی");
        Author author3 = new Author(1567, "نورالله عباس‌زاده");
        Author author4 = new Author(1643, "خلیل جلیل‌الهی");
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("3134", "Improving Every Thing", Arrays.asList(author1, author2)));
        articles.add(new Article("3454", "Improving Gravity", Arrays.asList(author3, author4, author1)));
        articles.add(new Article("3899", "No Improvement In Every Aspects Of Nothing", Arrays.asList(author2, author3, author1)));
        return articles;*//*

       return null;
    }

    public PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest) {
        List<Link> links = new ArrayList<>();
        Random random = new Random();
        int topicSize = predictedLinksRequest.getTopic_size();
        for (int i = 0; i < predictedLinksRequest.getLinks_number(); i++) {
            Node node1 = createNode(1, topicSize);
            Node node2 = createNode(2, topicSize);
            links.add(new Link(node1, node2, random.nextDouble()));
        }
        return new PredictedLinks(links);
    }

    private Node createNode(int type, int size) {
        List<WordProbability> wordList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            wordList.add(new WordProbability("word" + type + i, .1));
        }
        return new Node(wordList);
    }

    public List<Author> getCoAuthors(String code) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1234, "عباس نوری"));
        authors.add(new Author(1245, "جلیل جلیلی"));
        authors.add(new Author(1567, "نورالله عباس‌زاده"));
        authors.add(new Author( 1643, "خلیل جلیل‌الهی"));
        return authors;
    }

    public List<Author> getPredictedCoAuthors(String code) {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1244, "چبران جابر"));
        return authors;
    }

    public Article getArticleInfo(Integer code) {
        Author author1 = new Author(1234, "عباس نوری");
        Author author2 = new Author(1245, "جلیل جلیلی");
        return null;
//        return new Article("3899", "No Improvement In Every Aspects Of Nothing", Arrays.asList(author2, author1));
    }

    public List<Article> getRelatedArticles(String code) {
        Author author1 = new Author(1234, "عباس نوری");
        Author author2 = new Author(1245, "جلیل جلیلی");
        Author author3 = new Author(1567, "نورالله عباس‌زاده");
        Author author4 = new Author(1643, "خلیل جلیل‌الهی");
        List<Article> articles = new ArrayList<>();
//        articles.add(new Article("567", "Related Article to Every Thing", Arrays.asList(author1, author2)));
//        articles.add(new Article("7644", "Improving Gravity In No Where", Arrays.asList(author3, author4, author1)));
//        return articles;
        return null;
    }

    public List<Article> getPredictedRelatedArticles(String code) {
        Author author1 = new Author(1234, "عباس نوری");
        Author author2 = new Author(1245, "جلیل جلیلی");
        List<Article> articles = new ArrayList<>();
//        articles.add(new Article("70998", "Improvement Does Not Matter", Arrays.asList(author1, author2)));
//        return articles;
        return null;
    }

    public List<String> getAuthorTopic(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4", "word5", "word6", "word7", "word8", "word9", "word10", "word11", "word12");
    }

    public List<String> getArticleTopic(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4", "word5", "word6", "word7", "word8");
    }

    public List<String> getAuthorTopicCcs(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4", "word5", "word6", "word7", "word8", "word9", "word10");
    }

    public List<String> getArticleTopicCcs(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4");
    }

    public List<String> getAuthorTopicKeywords(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4", "word5", "word6", "word7", "word8", "word9", "word10");
    }

    public List<String> getArticleTopicKeywords(String code) {
        return Arrays.asList("word1", "word2", "word3", "word4");
    }

    public List<TopicProbability> getAuthorTopicProbability(String code) {
        return getTopicProbabilities();
    }

    public List<TopicProbability> getArticleTopicProbability(String code) {
        return getTopicProbabilities();
    }

    private List<TopicProbability> getTopicProbabilities() {
        List<String> words = Arrays.asList("word1", "word2", "word3", "word4", "word5");
        List<TopicProbability> topicProbabilities = new ArrayList<>();
        topicProbabilities.add(new TopicProbability("1", .02, words));
        topicProbabilities.add(new TopicProbability("2", .02, words));
        topicProbabilities.add(new TopicProbability("3", .02, words));
        topicProbabilities.add(new TopicProbability("4", .02, words));
        topicProbabilities.add(new TopicProbability("5", .4, words));
        topicProbabilities.add(new TopicProbability("6", .2, words));
        topicProbabilities.add(new TopicProbability("7", .01, words));
        topicProbabilities.add(new TopicProbability("8", .005, words));
        topicProbabilities.add(new TopicProbability("9", .3, words));
        topicProbabilities.add(new TopicProbability("10", .005, words));
        return topicProbabilities;
    }

*/
}
