package ch.zhaw.card2brain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 The main class of the Card2Brain application, containing the main method and the CorsFilter bean configuration.
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.12.2023
 */

@SpringBootApplication
public class Card2BrainApplication {


    public static void main(String[] args) {
        SpringApplication.run(Card2BrainApplication.class, args);


    }

}
