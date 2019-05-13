package asad.scheduledServices;

import asad.model.Link;
import asad.model.dataaccess.entity.Article;
import asad.model.dataaccess.entity.ArticlesPredictedLink;
import asad.model.dataaccess.entity.Author;
import asad.model.dataaccess.entity.AuthorsPredictedLink;
import asad.model.dataaccess.repository.ArticlePredictedLinkRepository;
import asad.model.dataaccess.repository.ArticleRepository;
import asad.model.dataaccess.repository.AuthorPredictedLinkRepository;
import asad.model.dataaccess.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * @author Reza Shekarchian
 */
@Service
public class GraphService {

    @Autowired
    private AuthorPredictedLinkRepository authorPredictedLinkRepository;
    @Autowired
    private ArticlePredictedLinkRepository articlePredictedLinkRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleRepository articleRepository;

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

    public void createPredictedAuthorsTable() {
        Properties properties = getTopicModelingPropertyFile();
//        String topicModelingInputFile = properties.getProperty("article.topic_modeling.files.path") + "/topics.txt";

        File dir = new File(properties.getProperty("author.link_prediction.files.path"));
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("input-");
            }
        });

        List<AuthorsPredictedLink> authorsPredictedLinks = new ArrayList<>();
        String line = null;
        try (FileReader fr = new FileReader(foundFiles[0]);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                String[] splitedLine = line.split("\t");
                if (Double.valueOf(splitedLine[2]) < .5) {
                    break;
                }
                AuthorsPredictedLink apl = new AuthorsPredictedLink(
                        (Integer.valueOf(splitedLine[0])),
                        (Integer.valueOf(splitedLine[1])),
                        Double.valueOf(splitedLine[2]));
                authorsPredictedLinks.add(apl);

            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        authorPredictedLinkRepository.saveAll(authorsPredictedLinks);
    }

    public Map<Link, Integer> getCoAuthorsGraph(Iterable<Author> authors) {
        Map<Link, Integer> authorsGraph = new HashMap<>();
        authors.forEach(author -> {
            Map<Author, Integer> coAuthors = getMapOfCoAuthors(author.getArticles(), author.getId());
            coAuthors.forEach((ca, w) -> {
                if (w > 1)
                    System.out.println(ca.getId());
                Link link = new Link(author.getId(), ca.getId());
                if (!authorsGraph.containsKey(link)) {
                    authorsGraph.put(link, w);
                }
            });
        });
        return authorsGraph;
    }

    private Map<Author, Integer> getMapOfCoAuthors(Set<Article> articles, Integer authorId) {
        Map<Author, Integer> authorsMap = new HashMap<>();
        articles.forEach((article -> {
            article = articleRepository.findArticleCompleteInfo(article.getId());
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

    public void createCoAuthorsGraphFile() {
        Properties properties = getTopicModelingPropertyFile();
        Iterable<Author> authors = authorRepository.findAllAuthorsArticles();
        Map<Link, Integer> coAuthorGraph = getCoAuthorsGraph(authors);
        Map<Integer, Integer> authorsSeqNum = new HashMap<>();
        String topicModelingInputFile = properties.getProperty("author.link_prediction.files.path") + "/input.net";
        try (FileWriter fw = new FileWriter(topicModelingInputFile, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)
        ) {
            out.println("*Vertices " + ((Set<Author>) authors).size());
            final int[] counter = {0};
            authors.forEach(author -> {
                counter[0]++;
                authorsSeqNum.put(author.getId(), counter[0]);
                out.println(counter[0] + " " + author.getId());
            });
            out.println("*Edges");
            coAuthorGraph.forEach((link, weight) -> {
                out.println(authorsSeqNum.get(link.getNode1Id()) + " " + authorsSeqNum.get(link.getNode2Id()) + " " + weight);
            });
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }


    }

    public void createRelatedArticlesGraphFile() {
        Properties properties = getTopicModelingPropertyFile();
        Iterable<Article> articles = articleRepository.findAllArticlesAuthors();
        Map<Link, Integer> articleGraph = getArticlesGraph(articles);
        Map<Integer, Integer> articleSeqNum = new HashMap<>();
        String linkPredictionInputFile = properties.getProperty("article.link_prediction.files.path") + "/input.net";
        try (FileWriter fw = new FileWriter(linkPredictionInputFile, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)
        ) {
            out.println("*Vertices " + ((Set<Article>) articles).size());
            final int[] counter = {0};
            articles.forEach(article -> {
                counter[0]++;
                articleSeqNum.put(article.getId(), counter[0]);
                out.println(counter[0] + " " + article.getId());
            });
            out.println("*Edges");
            articleGraph.forEach((link, weight) -> {
                out.println(articleSeqNum.get(link.getNode1Id()) + " " + articleSeqNum.get(link.getNode2Id()) + " " + weight);
            });
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public Map<Link, Integer> getArticlesGraph(Iterable<Article> articles) {
        Map<Link, Integer> articlesGraph = new HashMap<>();
        articles.forEach(article -> {
            Map<Article, Integer> relatedArticle = getMapRelatedArticles(article.getAuthors(), article.getId());
            relatedArticle.forEach((ra, w) -> {
                if (w > 1)
                    System.out.println(ra.getId());
                Link link = new Link(article.getId(), ra.getId());
                if (!articlesGraph.containsKey(link)) {
                    articlesGraph.put(link, w);
                }
            });
        });
        return articlesGraph;
    }

    private Map<Article, Integer> getMapRelatedArticles(Set<Author> authors, Integer articleId) {
        Map<Article, Integer> articlesMap = new HashMap<>();
        authors.forEach((author -> {
            author = authorRepository.findAuthorArticles(author.getId());
            Set<Article> authorArticles = author.getArticles();
            authorArticles.forEach(article -> {
                if (article.getId().equals(articleId))
                    return;
                if (!articlesMap.containsKey(article)) {
                    articlesMap.put(article, 1);
                } else {
                    System.out.println("here");
                    articlesMap.put(article, articlesMap.get(article) + 1);
                }
            });
        }));
        return articlesMap;
    }

    public void createArticlesPredictedLinkTable() {
        Properties properties = getTopicModelingPropertyFile();
//        String topicModelingInputFile = properties.getProperty("article.topic_modeling.files.path") + "/topics.txt";

        File dir = new File(properties.getProperty("article.link_prediction.files.path"));
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("input-");
            }
        });

        List<ArticlesPredictedLink> articlesPredictedLinks = new ArrayList<>();
        String line = null;
        try (FileReader fr = new FileReader(foundFiles[0]);
             BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {
                String[] splitedLine = line.split("\t");
                if (Double.valueOf(splitedLine[2]) < .3) {
                    break;
                }
                ArticlesPredictedLink apl = new ArticlesPredictedLink(
                        (Integer.valueOf(splitedLine[0])),
                        (Integer.valueOf(splitedLine[1])),
                        Double.valueOf(splitedLine[2]));
                articlesPredictedLinks.add(apl);

            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
        articlePredictedLinkRepository.saveAll(articlesPredictedLinks);
    }
}
