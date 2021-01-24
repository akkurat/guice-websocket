package ch.taburett.gameworld;

import ch.taburett.gameworld.wiring.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@ServletComponentScan
@SpringBootApplication
@ComponentScan({"ch.taburett.gameworld.endpoints",
        "ch.taburett.gameworld.wiring",
        "ch.taburett.gameworld.services",
})
public class GameWorld {

    private static  final Logger logger = LoggerFactory.getLogger(GameWorld.class);
    public static void main(String[] args) {
        SpringApplication.run(GameWorld.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, UserService userService) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            // fetch all users
            logger.info("Users found with findAll():");
            logger.info("---------------------------");
//            for (User user : userService.findAll()) {
//                logger.info(user.toString());
//            }
            logger.info("");


        };
    }

}