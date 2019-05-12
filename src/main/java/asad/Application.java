package asad;

import asad.scheduledServices.TopicModelingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    TopicModelingService topicModelingService;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

//        topicModelingService.createDenormalizedLemmatizedArticleText();//todo drop table before run
//////////////////////////////////////////////////////////////////////

//        topicModelingService.createAuthorBasedInputForTopicModeling();

/////////////////////////////////////////////////////////////////////////////////////

        // topicModelingService.createArticlesBasedInputForTopicModeling();

// Drop tables
///////////////////////////////////////////////////////create RPC
//        topicModelingService.deleteTopicRecords();
//        topicModelingService.createArticleTopicsTable();
//        topicModelingService.createArticlesTopicDistribution();

///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

//        topicModelingService.createAuthorTopicsTable();
//        topicModelingService.createAuthorsTopicDistribution();

        /////////////////////////////////////////////////////////////////////////////////////// link prediction

//        topicModelingService.createCoAuthorsGraphFile();

//        //////////////////////////////////////
        topicModelingService.createPredictedAuthorsTable();

        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };

    }

}
