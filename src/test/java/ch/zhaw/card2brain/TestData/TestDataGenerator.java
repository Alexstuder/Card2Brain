package ch.zhaw.card2brain.TestData;

import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;

import java.util.ArrayList;
import java.util.List;
/**

 The {@code TestDataGenerator} class is responsible for generating test data for unit tests.
 It provides methods for generating default instances of {@link User}, {@link Category}, and {@link Card} classes.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
public class TestDataGenerator {

    /** The default username. */
    public static final String DEFAULT_USER_NAME = "User1";

    /** The default category name. */
    public static final String DEFAULT_CATEGORY1 = "Mathematik";

    /** The question string for cards. */
    public static final String QUESTION = "Frage";

    /** The answer string for cards. */
    public static final String ANSWER = "Antwort";

    /**

     Returns the default {@link User} instance.
     @return The default user instance
     */
    public static User GET_DEFAULT_USER() {
        return GET_TEST_USER(DEFAULT_USER_NAME);
    }


    /**

     Returns a {@link User} instance with the specified name.
     @param name The name of the user
     @return The user instance with the specified name
     */
    public static User GET_TEST_USER(String name) {
        User testUser = new User();
        testUser.setUserName(name);
        testUser.setFirstName("FirstName1");
        testUser.setMailAddress("Mail@Address.1");
        testUser.setPassword("Password");
        return testUser;
    }

    /**
     Returns the default {@link Category} instance.
     @return The default category instance
     */
    public static Category GET_DEFAULT_CATEGORY() {
        return GET_TEST_CATEGORY(DEFAULT_CATEGORY1, TestDataGenerator.GET_DEFAULT_USER());
    }
    /**

     Returns the default {@link Category} instance associated with the specified {@link User}.
     @param user The user to associate the category with
     @return The default category instance associated with the specified user
     */

    public static Category GET_DEFAULT_CATEGORY_TO_USER(User user) {
        return GET_TEST_CATEGORY(DEFAULT_CATEGORY1, user);
    }

    /**

     Returns a {@link Category} instance with the specified name and associated with the specified {@link User}.
     @param categoryName The name of the category
     @param user The user to associate the category with
     @return The category instance with the specified name and associated with the specified user
     */
    public static Category GET_TEST_CATEGORY(String categoryName, User user) {
        return new Category(categoryName, user);
    }
    /**
     Returns the default {@link Card} instance.
     @return The default card instance
     */
    public static Card GET_DEFAULT_CARD() {
        return GET_TEST_CARD(TestDataGenerator.GET_DEFAULT_CATEGORY());
    }
    /**
     *
     Returns a {@link Card} object created with the given category.
     @param category - The {@link Category} object to associate the {@link Card} with
     @return a {@link Card} object created with the given category
     */
    public static Card GET_TEST_CARD(Category category) {
        return new Card("Frage", "Antwort", category);
    }
    /**
     *

     Returns a list of 50 test {@link Card} objects created with the given category.
     @param category - The {@link Category} object to associate the {@link Card} with
     @return list of 50 test {@link Card} objects created with the given category
     */
    public static List<Card> GET_TEST_CARDS(Category category) {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            Card card = new Card(QUESTION + " " + i, ANSWER + " " + i, category);
            cards.add(card);
        }

        return cards;
    }
}
