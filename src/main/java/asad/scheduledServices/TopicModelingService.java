package asad.scheduledServices;

import asad.model.dataaccess.entity.*;
import asad.model.dataaccess.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * @author Reza Shekarchian
 */
@Service
public class TopicModelingService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleKeywordRepository articleKeywordRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ArticleTopicDistributionRepository articleTopicDistributionRepository;
    @Autowired
    private DenormalizedArticleRepository denormalizedArticleRepository;
    @Autowired
    private AuthorTopicDistributionRepository authorTopicDistributionRepository;

    private StanfordLemmatizer lemmatizer = new StanfordLemmatizer();

    public void createDenormalizedLemmatizedArticleText() {
        Set<Article> articles = articleRepository.findAllArticlesWithTaxonomy();
        articles.forEach(article -> {
            Set<ArticleKeyword> articleKeywords = articleKeywordRepository.findByArticle_Id(article.getId());
            denormalizedArticleRepository.save(new DenormalizedLemmatizedArticleText(
                    article.getId(),
                    lemmatizer.lemmatize(article.getTitle()),
                    lemmatizer.lemmatize(article.getAbstractColumn()),
                    lemmatizer.lemmatize(listTaxonomyToString(article.getTaxonomies())),
                    lemmatizer.lemmatize(listKeywordsToString(articleKeywords))
            ));
        });
    }

    private String listKeywordsToString(Set<ArticleKeyword> keywords) {
        String result = "";
        for (ArticleKeyword element : keywords) {
            result += element.getKeyword() + " ";
        }
        return result;
    }

    private String listTaxonomyToString(Set<Taxonomy> taxonomies) {
        String result = "";
        for (Taxonomy element : taxonomies) {
            result += element.getTitle() + " ";
        }
        return result;
    }

    public void createAuthorBasedInputForTopicModeling() {
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingInputFile = properties.getProperty("author.topic_modeling.files.path") + "/input.txt";
        Set<Author> authors = authorRepository.findAllAuthorsArticles();
        try (FileWriter fw = new FileWriter(topicModelingInputFile, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            authors.forEach(author -> {
                out.println(getAuthorTextForTopicModelingInput(author));
            });

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public void createArticlesBasedInputForTopicModeling() { // todo update it with lemmatized
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingInputFile = properties.getProperty("article.topic_modeling.files.path") + "/input.txt";
        Set<Article> articles = articleRepository.findAllArticlesWithKeyword();
        try (FileWriter fw = new FileWriter(topicModelingInputFile, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            articles.forEach(article -> {
                out.println(getArticleTextForTopicModelingInput(article));
            });

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    private String getAuthorTextForTopicModelingInput(Author author) {
        String line = "";
        String text = "";
        line += "author-" + author.getId() + "\t" + "X\t";
        for (Article article : author.getArticles()) {
            DenormalizedLemmatizedArticleText dla = denormalizedArticleRepository.findByArticleId(article.getId());
            text += dla.getTitle() + " " + dla.getAbstractColumn() + " " + dla.getTaxonomies() + " " +
                    dla.getKeyword() + " ";

        }
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            line += scanner.nextLine() + " ";
            // process the line
        }
        scanner.close();
        return line;

    }


    public void deleteTopicRecords() {
        Iterable<Topic> topics = topicRepository.findAll();
        topics.forEach(t -> topicRepository.delete(t));
    }

    public void createArticleTopicsTable() {
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingInputFile = properties.getProperty("article.topic_modeling.files.path") + "/topics.txt";
        String line = null;
        try (FileReader fr = new FileReader(topicModelingInputFile);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                String[] splitedLine = line.split("\t");
                Topic topic = new Topic(Integer.parseInt(splitedLine[0]), Topic.Type.article, splitedLine[2]);
                topicRepository.save(topic);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public void createAuthorTopicsTable() {
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingInputFile = properties.getProperty("author.topic_modeling.files.path") + "/topics.txt";
        String line = null;
        try (FileReader fr = new FileReader(topicModelingInputFile);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                String[] splitedLine = line.split("\t");
                Topic topic = new Topic(Integer.parseInt(splitedLine[0]), Topic.Type.author, splitedLine[2]);
                topicRepository.save(topic);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public void createArticlesTopicDistribution() {
        Map<Integer, Topic> topicMap = getTopics(Topic.Type.article);
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingTopicCompositionFile = properties.getProperty("article.topic_modeling.files.path") + "/topic-composition.txt";
//        String topicModelingTopicInputFile = properties.getProperty("article.topic_modeling.files.path") + "/input.txt";
        String topicCompositionLine = null;
//        String topicInputLine = null;
        try (FileReader topicCompositioniFileReader = new FileReader(topicModelingTopicCompositionFile);
             BufferedReader topicCompositionBufferReader = new BufferedReader(topicCompositioniFileReader);
//             FileReader topicInputFileReader = new FileReader(topicModelingTopicInputFile);
//             BufferedReader topicInputBufferReader = new BufferedReader(topicInputFileReader)
        ) {
            List<ArticleTopicDistribution> articleTopicDistributions = new ArrayList<>();
            while ((topicCompositionLine = topicCompositionBufferReader.readLine()) != null) {
                try {
//                    topicInputLine = topicInputBufferReader.readLine();
                    String[] splitedLine = topicCompositionLine.split("\t");
                    Integer articleId = extractArticleId(splitedLine[1]);
                    Optional<Article> article = articleRepository.findById(articleId);
                    for (int i = 0; i < splitedLine.length - 2; i++) {
                        ArticleTopicDistribution articleTopicDistribution = new ArticleTopicDistribution(
                                article.get(), topicMap.get(i), Double.parseDouble(splitedLine[i + 2]));
                        articleTopicDistributions.add(articleTopicDistribution);
//                        articleTopicDistributionRepository.save(articleTopicDistribution);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println(topicCompositionLine);
                }
            }
            articleTopicDistributionRepository.saveAll(articleTopicDistributions);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    public void createAuthorsTopicDistribution() {
        Map<Integer, Topic> topicMap = getTopics(Topic.Type.author);
        Properties properties = getTopicModelingPropertyFile();
        String topicModelingTopicCompositionFile = properties.getProperty("author.topic_modeling.files.path") + "/topic-composition.txt";
        String topicCompositionLine = null;
        try (FileReader topicCompositioniFileReader = new FileReader(topicModelingTopicCompositionFile);
             BufferedReader topicCompositionBufferReader = new BufferedReader(topicCompositioniFileReader);
        ) {
            List<AuthorTopicDistribution> authorTopicDistributions = new ArrayList<>();
            while ((topicCompositionLine = topicCompositionBufferReader.readLine()) != null) {
                try {
                    String[] splitedLine = topicCompositionLine.split("\t");
                    Integer authorId = extractAuthorleId(splitedLine[1]);
                    Optional<Author> author = authorRepository.findById(authorId);
                    for (int i = 0; i < splitedLine.length - 2; i++) {
                        AuthorTopicDistribution authorTopicDistribution = new AuthorTopicDistribution(
                                author.get(), topicMap.get(i), Double.parseDouble(splitedLine[i + 2]));
                        authorTopicDistributions.add(authorTopicDistribution);
//                        articleTopicDistributionRepository.save(articleTopicDistribution);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println(topicCompositionLine);
                }
            }
            authorTopicDistributionRepository.saveAll(authorTopicDistributions);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    private Integer extractArticleId(String text) {
        return Integer.parseInt(text.substring("article-".length()));
    }

    private Integer extractAuthorleId(String text) {
        return Integer.parseInt(text.substring("author-".length()));
    }
    private Map getTopics(Topic.Type type) {
        List<Topic> topics = topicRepository.findByType(type);
        Map<Integer, Topic> topicMap = new HashMap<>();
        topics.forEach(t -> topicMap.put(t.getTopicCode(), t));
        return topicMap;
    }

    private String getArticleTextForTopicModelingInput(Article article) {
        String line = "";
        String text = "";
        line += "article-" + article.getId() + "\t" + "X\t";
        DenormalizedLemmatizedArticleText dla = denormalizedArticleRepository.findByArticleId(article.getId());
        text += dla.getTitle() + " " + dla.getAbstractColumn() + " " + dla.getTaxonomies() + " " +
                dla.getKeyword() + " ";
        Scanner scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            line += scanner.nextLine() + " ";
            // process the line
        }
        scanner.close();
        return line;
    }

    private Properties getTopicModelingPropertyFile() {
        Properties prop = null;
        try (InputStream input = new FileInputStream("src/main/resources/TopicModel.properties")) {
            prop = new Properties();
            prop.load(input);
//            System.out.println(prop.getProperty("article.topic_modeling.files.path"));
//            System.out.println(prop.getProperty("author.topic_modeling.files.path"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    public void createCoAuthorsGraphFile() {

    }

}
