package asad.services;

import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.ArticleKeyword;
import asad.model.dataaccess.repository.ArticleKeywordRepository;
import asad.model.dataaccess.repository.ArticleRepository;
import asad.model.dataaccess.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;
import java.util.Set;

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

//    public void read top

    private String getArticleTextForTopicModelingInput(Article article) {
        String text = "";
        text += "article-" + article.getId() + "\t" + "X\t";
        text += article.getTitle() + " "
                + article.getAbstractColumn() + " ";
        for (ArticleKeyword keyword : article.getKeyword()) {
            text += keyword.getKeyword() + " ";
        }
        text.replace("\n", "")
                .replace("\r", "")
                .replace("\\", "")
                .replace("$", "")
                .replace("{", "")
                .replace("}", "");

        return text;
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
