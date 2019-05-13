package asad.services;

import asad.model.PredictedLinks;
import asad.model.PredictedLinksRequest;
import asad.model.TopicProbability;
import asad.model.dataaccess.entity.*;
import asad.model.dataaccess.repository.*;
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

    @Autowired
    private AuthorTopicDistributionRepository authorTopicDistributionRepository;

    @Autowired
    private AuthorPredictedLinkRepository authorPredictedLinkRepository;

    @Autowired
    private ArticlePredictedLinkRepository articlePredictedLinkRepository;

    private List<Integer> rootTopicsId = Arrays.asList(new Integer[]{2902, 2922, 3057, 3374, 3450, 3558, 3664, 3793, 3979, 4210, 4345, 4402, 4616});

    public AuthorWrapper getAuthorInfo(Integer id) {
        Author author = authorRepository.findById(id).get();
        return new AuthorWrapper(author);
    }

    @Override
    public Set<ArticleWrapper> getAuthorArticles(Integer id) {
        Set<Article> articles = authorRepository.findAuthorArticlesAuthors(id).getArticles();
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
        Set<Article> articles = authorRepository.findAuthorArticlesAuthors(Integer.parseInt(code)).getArticles();
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
        articleTopicDistributions.forEach(atd -> {
            topicProbabilities.add(new TopicProbability(atd.getTopic().getTopicCode(), atd.getProbability(),
                    Arrays.asList(atd.getTopic().getWordList().split(" "))));
        });
        return topicProbabilities;
    }


    @Override
    public List<String> getArticleTopic(String code) {
        List<String> topicTopWords = getArticleTopWordsOfTopic(code);
        Set<String> keyword = getArticleTopicKeywords(code);
        Set<String> ccs = getArticleTopicCcs(code);
        List<String> mergedTopics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        keyword.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 5)
                return;
        });
        counter.set(0);
        ccs.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 5)
                return;
        });
        counter.set(0);
        topicTopWords.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 5)
                return;
        });

        return mergedTopics;

    }

    private List<String> getArticleTopWordsOfTopic(String code) {
        Map<Double, String> topicProbabilitiesMap = new HashMap<>();
        List<ArticleTopicDistribution> articleTopicDistributions = articleTopicDistributionRepository.findArticleTopic(
                Integer.parseInt(code));
        articleTopicDistributions.forEach(atd -> {
            topicProbabilitiesMap.put(atd.getProbability(), atd.getTopic().getWordList());
        });
        List<String> sortedTopics = getSortedListFromTopicsMap(topicProbabilitiesMap);
        List<String> topWords = new ArrayList<>();
        String[] splitedWords = sortedTopics.get(0).split(" ");
        for (int i = 0; i < 7; i++) {
            topWords.add(splitedWords[i]);
        }
        splitedWords = sortedTopics.get(1).split(" ");
        for (int i = 0; i < 3; i++) {
            topWords.add(splitedWords[i]);
        }
        return topWords;
    }

    private List<String> getSortedListFromTopicsMap(Map<Double, String> topicProbabilitiesMap) {
        Map<Double, String> reverseSortedMap = new TreeMap<Double, String>(Collections.reverseOrder());
        reverseSortedMap.putAll(topicProbabilitiesMap);
        List<String> topicList = new ArrayList<>();
        reverseSortedMap.forEach((p, w) -> {
            topicList.add(w);
        });
        return topicList;
    }

    @Override
    public List<TopicProbability> getAuthorTopicProbability(String code) {
        List<TopicProbability> topicProbabilities = new ArrayList<>();
        List<AuthorTopicDistribution> authorTopicDistributions = authorTopicDistributionRepository.findAuthorTopic(
                Integer.parseInt(code));
        authorTopicDistributions.forEach(atd -> {
            topicProbabilities.add(new TopicProbability(atd.getTopic().getTopicCode(), atd.getProbability(),
                    Arrays.asList(atd.getTopic().getWordList().split(" "))));
        });
        return topicProbabilities;
    }

    @Override
    public List<String> getAuthorTopic(String code) {
        List<String> topicTopWords = getAuthorleTopWordsOfTopic(code);
        Set<String> keyword = getAuthorTopicKeywords(code);
        Set<String> ccs = getAuthorTopicCcs(code);
        List<String> mergedTopics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        counter.set(0);
        keyword.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 10)
                return;
        });
        counter.set(0);
        ccs.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 10)
                return;
        });
        counter.set(0);
        topicTopWords.forEach(w -> {
            mergedTopics.add(w);
            counter.getAndIncrement();
            if (counter.get() > 10)
                return;
        });

        return mergedTopics;

    }

    private List<String> getAuthorleTopWordsOfTopic(String code) {
        Map<Double, String> topicProbabilitiesMap = new HashMap<>();
        List<AuthorTopicDistribution> authorTopicDistributions = authorTopicDistributionRepository.findAuthorTopic(
                Integer.parseInt(code));
        authorTopicDistributions.forEach(atd -> {
            topicProbabilitiesMap.put(atd.getProbability(), atd.getTopic().getWordList());
        });
        List<String> sortedTopics = getSortedListFromTopicsMap(topicProbabilitiesMap);
        List<String> topWords = new ArrayList<>();
        String[] splitedWords = sortedTopics.get(0).split(" ");
        for (int i = 0; i < 7; i++) {
            topWords.add(splitedWords[i]);
        }
        splitedWords = sortedTopics.get(1).split(" ");
        for (int i = 0; i < 3; i++) {
            topWords.add(splitedWords[i]);
        }
        return topWords;
    }


    @Override
    public List<Author> getPredictedCoAuthors(Integer code) {
        List<Author> authors = new ArrayList<>();
        List<AuthorsPredictedLink> apl = authorPredictedLinkRepository.findByAuthorId((code));
        apl.forEach(link->{
            if (link.getAuthor1().equals(code))
                authors.add(authorRepository.findById(link.getAuthor2()).get());
            else
                authors.add(authorRepository.findById(link.getAuthor1()).get());
        });
        return authors;
    }

    @Override
    public Set<Article> getRelatedArticles(String code) {

        Set<Author> authors = articleRepository.findArticleAuthors(Integer.parseInt(code)).getAuthors();
        Map<Article, Integer> articlesMap = createMapOfArticles(authors, Integer.parseInt(code));
        Map<Article, Integer> sortedAuthorsMap = sortArticleMapByValue(articlesMap);
        return sortedAuthorsMap.keySet();

    }

    private static Map<Article, Integer> sortArticleMapByValue(final Map<Article, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<Article, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<Article, Integer> createMapOfArticles(Set<Author> authors, Integer articleId) {
        Map<Article, Integer> articlesMap = new HashMap<>();
        authors.forEach((author -> {
            Set<Article> authorArticles = author.getArticles();
            authorArticles.forEach(article -> {
                if (article.getId().equals(articleId))
                    return;
                if (!articlesMap.containsKey(article)) {
                    articlesMap.put(article, 1);
                } else {
                    articlesMap.put(article, articlesMap.get(article) + 1);
                }
            });
        }));
        return articlesMap;
    }


    @Override
    public List<Article> getPredictedRelatedArticles(Integer code) {
        List<Article> articles = new ArrayList<>();
        List<ArticlesPredictedLink> apl = articlePredictedLinkRepository.findByArticleId((code));
        apl.forEach(link->{
            if (link.getArticle1().equals(code))
                articles.add(articleRepository.findById(link.getArticle2()).get());
            else
                articles.add(articleRepository.findById(link.getArticle1()).get());
        });
        return articles;

    }

    @Override
    public PredictedLinks getPredictedLinks(PredictedLinksRequest predictedLinksRequest) {
        return null;
    }



}
