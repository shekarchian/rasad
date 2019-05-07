package asad.scheduledServices;

import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.ArticleKeyword;
import asad.model.dataaccess.entity.ArticleTopicDistribution;
import asad.model.dataaccess.entity.Topic;
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
    private StanfordLemmatizer stanfordLemmatizer = new StanfordLemmatizer();

    public void createArticlesBasedInputForTopicModeling() {
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

    public void createArticlesTopicDistribution() {
        Map<Integer, Topic> topicMap = getArticleTopics();
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

    private Integer extractArticleId(String text) {
        return Integer.parseInt(text.substring("article-".length()));
    }

    private Map getArticleTopics() {
        List<Topic> topics = topicRepository.findByType(Topic.Type.article);
        Map<Integer, Topic> topicMap = new HashMap<>();
        topics.forEach(t -> topicMap.put(t.getTopicCode(), t));
        return topicMap;
    }

    private String getArticleTextForTopicModelingInput(Article article) {
        String line = "";
        String text = "";
        line += "article-" + article.getId() + "\t" + "X\t";
        text += article.getTitle() + " "
                + article.getAbstractColumn() + " ";
        for (ArticleKeyword keyword : article.getKeyword()) {
            text += keyword.getKeyword() + " ";
        }
        List<String> lemma = stanfordLemmatizer.lemmatize(text);
        String lemmaText = "";
        for (String w: lemma) {
            lemmaText = lemmaText + w + " ";
        }
        Scanner scanner = new Scanner(lemmaText);
        while (scanner.hasNextLine()) {
            line += scanner.nextLine() + " ";
            // process the line
        }
        scanner.close();
//        text.replaceAll("\n", " ")
//                .replaceAll("\r", " ")
//                .replaceAll("\r\n", " ")
//                .replaceAll("\\r\\n|\\r|\\n", " ")
//                .replaceAll(System.getProperty("line.separator"), " ");
//        line = line + text;
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
}
