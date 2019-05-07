package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.dataaccess.entity.*;
import asad.model.dataaccess.repository.ArticleKeywordRepository;
import asad.model.dataaccess.repository.ArticleRepository;
import asad.model.dataaccess.repository.ArticleTopicDistributionRepository;
import asad.model.dataaccess.repository.AuthorRepository;
import asad.model.wrapper.ArticleWrapper;
import asad.model.wrapper.AuthorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LinkPredictionServiceImpl implements LinkPredictionService {


    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleKeywordRepository articleKeywordRepository;

    @Autowired
    private ArticleTopicDistributionRepository articleTopicDistributionRepository;


    private List<Integer> rootTopicsId = Arrays.asList(new Integer[]{2902, 2922, 3057, 3374, 3450, 3558, 3664, 3793, 3979, 4210, 4345, 4402, 4616});

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
        if (taxonomies.size() < 3) {
            articleTaxonomies.forEach((taxonomy) -> {
                if (rootTopicsId.contains(taxonomy.getParent_taxonomy_class_id()))
                    taxonomies.add(taxonomy.getTitle());
            });
        }
        return taxonomies;
    }

    @Override
    public Set<String> getAuthorTopicKeywords(String code) {
        Set<ArticleKeyword> articleKeywords = articleKeywordRepository.findAuthorKeywords(Integer.parseInt(code));
        Set<String> keywords = new HashSet<>();
        articleKeywords.forEach((keyword) -> keywords.add(keyword.getKeyword()));
        return keywords;
    }

    @Override
    public Set<String> getAuthorTopicCcs(String code) {
        Set<Taxonomy> authorTaxonomies = authorRepository.findAuthorTaxonomies(Integer.parseInt(code));
        Set<String> taxonomies = new HashSet<>();
        authorTaxonomies.forEach((taxonomy) -> {
            if (!rootTopicsId.contains(taxonomy.getId()) && !rootTopicsId.contains(taxonomy.getParent_taxonomy_class_id()))
                taxonomies.add(taxonomy.getTitle());
        });
        return taxonomies;
    }

    @Override
    public Set<Author> getCoAuthors(String code) {
        Set<Article> articles = authorRepository.findAuthorArticles(Integer.parseInt(code)).getArticles();
        Map<Author, Integer> authorsMap = createMapOfAuthors(articles, Integer.parseInt(code));
        Map<Author, Integer> sortedAuthorsMap = sortByValue(authorsMap);
        return sortedAuthorsMap.keySet();
    }

    private Map<Author, Integer> createMapOfAuthors(Set<Article> articles, Integer authorId) {
        Map<Author, Integer> authorsMap = new HashMap<>();
        articles.forEach((article -> {
            Set<Author> articleAuthors = article.getAuthors();
            articleAuthors.forEach(author -> {
                if (author.getId().equals(authorId))
                    return;
                if (!authorsMap.containsKey(author)) {
                    authorsMap.put(author, 1);
                } else {
                    authorsMap.put(author, authorsMap.get(author) + 1);
                }
            });
        }));
        return authorsMap;
    }


    private static Map<Author, Integer> sortByValue(final Map<Author, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<Author, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    @Override
    public List<TopicProbability> getArticleTopicProbability(String code) {
        List<TopicProbability> topicProbabilities = new ArrayList<>();
        List<ArticleTopicDistribution> articleTopicDistributions = articleTopicDistributionRepository.findArticleTopic(
                Integer.parseInt(code));
        articleTopicDistributions.forEach(atd ->{
            topicProbabilities.add(new TopicProbability(atd.getTopic().getTopicCode(), atd.getProbability(),
                    Arrays.asList(atd.getTopic().getWordList().split(" "))));
        });
        return topicProbabilities;
    }


    @Override
    public List<String> getArticleTopic(String code) {
        List<String> topicTopWords = getTopWordsOfTopic(code);
        Set<String> keyword = getArticleTopicKeywords(code);
        Set<String> ccs = getArticleTopicCcs(code);
        List<String> mergedTopics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        keyword.forEach(w->{
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get()> 5)
                return;
        });
        counter.set(0);
        ccs.forEach(w->{
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get()> 5)
                return;
        });
        counter.set(0);
        topicTopWords.forEach(w->{
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get()> 5)
                return;
        });

        return mergedTopics;

    }

    private List<String> getTopWordsOfTopic(String code) {
        Map<Double, String> topicProbabilitiesMap = new HashMap<>();
        List<ArticleTopicDistribution> articleTopicDistributions = articleTopicDistributionRepository.findArticleTopic(
                Integer.parseInt(code));
        articleTopicDistributions.forEach(atd ->{
            topicProbabilitiesMap.put(atd.getProbability(), atd.getTopic().getWordList());
        });
        List<String> sortedTopics = getSortedListFromTopicsMap(topicProbabilitiesMap);
        List<String> topWords = new ArrayList<>();
        String[] splitedWords = sortedTopics.get(0).split(" ");
        for (int i = 0; i<7; i++) {
            topWords.add(splitedWords[i]);
        }
        splitedWords = sortedTopics.get(1).split(" ");
        for (int i = 0; i<3; i++) {
            topWords.add(splitedWords[i]);
        }
        return topWords;
    }

    private List<String> getSortedListFromTopicsMap(Map<Double, String> topicProbabilitiesMap) {
        Map<Double, String> reverseSortedMap = new TreeMap<Double, String>(Collections.reverseOrder());
        reverseSortedMap.putAll(topicProbabilitiesMap);
        List<String> topicList = new ArrayList<>();
        reverseSortedMap.forEach((p,w)->{
            topicList.add(w);
        });
        return topicList;
    }

    @Override
    public List<String> getAuthorTopic(String code) {
        return null;
    }

    @Override
    public List<TopicProbability> getAuthorTopicProbability(String code) {
        return null;
    }

    @Override
    public List<Article> getRelatedArticles(String code) {
        return null;
    }

    @Override
    public PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest) {
        return null;
    }

    @Override
    public List<Author> getPredictedCoAuthors(String code) {
        return null;
    }

    @Override
    public List<Article> getPredictedRelatedArticles(String code) {
        return null;
    }


}
