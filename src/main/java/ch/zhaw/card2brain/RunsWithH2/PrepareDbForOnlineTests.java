package ch.zhaw.card2brain.RunsWithH2;

import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * Class to prepare the database for online tests.
 * This class is marked as a Component and is only active for the "dev" profile.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 2023-01-29
 *
 * Component
 * Profile("dev")
 */
@Component
@Profile("dev")
public class PrepareDbForOnlineTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CardRepository cardRepository;

    private User user;

    private List<Category> categoryList;

    private List<Card> cards;

    /**
     * Method to fill the database with data when the application starts.
     * This method first deletes all data from the card, category and user repositories,
     * and then inserts the user, category and card data into the database.
     */

      @PostConstruct
    public void fillDbWithData() {

        cardRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(getUser());
        categoryRepository.saveAll(getCategory());
        cardRepository.saveAll(getCards());
    }
    /**
     * This method sets the test user's properties and returns the user object.
     * @return the user object
     */

    private User getUser() {
        this.user = new User("TestUser", "FirstName", "user@user.ch");
        this.user.setPassword("password");
        return user;
    }

    /**
     * This method sets the test categories' properties and returns a list of category objects.
     * @return the list of category objects with their properties set.
     */

    private List<Category> getCategory() {
        this.categoryList  = new ArrayList<>();
        this.categoryList.add(new Category("Französisch",user));
        this.categoryList.add(new Category("Deutsch",user));

        return this.categoryList;
    }

    /**
     * This method sets the test cards' properties and returns a list of card objects.
     * @return the list of card objects with their properties set.
     */

    private List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Frage1","Antwort1",categoryList.get(0)));
        cards.add(new Card("Frage2","Antwort2",categoryList.get(0)));
        cards.add(new Card("Frage1","Antwort1",categoryList.get(1)));
        cards.add(new Card("Frage2","Antwort2",categoryList.get(1)));
        return cards;
    }


}
