package ch.zhaw.card2brain;

import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * Card2BrainDbEntityTests class contains tests for the Card2Brain database entities.
 * This class extends EmptyDb, an abstract class that provides empty data
 * for testing purposes.
 * SpringBootTest annotation is used to indicate that this is a spring boot
 * test class.
 *
 * @author Alexander Studer
 * @author Niklaus Hänggi
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
@SpringBootTest
class Card2BrainDbEntityTests extends EmptyDb {
    final String DEUTSCH = "Deutsch";
    final String GESCHICHTE = "Geschichte";
    final String INFORMATIK = "Informatik";

    @Autowired
    UserService userService;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * Test method for the default user entity.
     * This test verifies if the default user is correctly persisted on the
     * database and can be retrieved and deleted.
     */
    @Test
    void testDefaultUser() {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);

        assertEquals(1, userRepository.findAll().size());

        userRepository.delete(user);
        assertEquals(0, userRepository.findAll().size());

    }


    /**
     * Test method for a custom user entity.
     * This test verifies if a custom user is correctly persisted on the
     * database and can be retrieved and deleted.
     */

    @Test
    void testCustomUser() {
        final String testUser = "Testee";
        User user = TestDataGenerator.GET_TEST_USER(testUser);
        userRepository.save(user);

        assertEquals(testUser, userRepository.findAll().get(0).getUserName());

        userRepository.delete(user);
        assertEquals(0, userRepository.findAll().size());

    }

    /**

     Test for category entity.
     Generates 3 categories and persists them in the database.
     It then asserts that the categories are saved successfully by checking their count in the database.
     It deletes one of the categories and checks if it has been deleted successfully.
     */
    @Test
    void testCategory() {

        //arrange
        final User TEST_USER = TestDataGenerator.GET_DEFAULT_USER();
        // genereate 3 categories and persist on DB
        Category category = TestDataGenerator.GET_TEST_CATEGORY(INFORMATIK, TEST_USER);
        Category category2 = TestDataGenerator.GET_TEST_CATEGORY(DEUTSCH, TEST_USER);
        Category category3 = TestDataGenerator.GET_TEST_CATEGORY(GESCHICHTE, TEST_USER);


        // persist user before persist category
        userRepository.save(category.getOwner());
        //persist 3 categories
        //test
        List<Category> categoriesToSaveOnDb = new ArrayList<>();
        categoriesToSaveOnDb.add(category);
        categoriesToSaveOnDb.add(category2);
        categoriesToSaveOnDb.add(category3);

        categoryRepository.saveAll(categoriesToSaveOnDb);

        //assert
        assertEquals(3, categoryRepository.findAll().size());


        List<Category> categoriesFromDb = categoryRepository.findCategoriesByOwner(category.getOwner());
        assertEquals(3, countRightCategoryNames(categoriesFromDb));

        //Delete first
        String deletedCategory = categoriesFromDb.get(0).getCategoryName();
        categoryRepository.deleteById(categoriesFromDb.get(0).getId());
        categoriesFromDb = categoryRepository.findCategoriesByOwner(category.getOwner());
        assertEquals(2, countRightCategoryNames(categoriesFromDb));
        assertTrue(isNotInList(categoriesFromDb, deletedCategory));
    }
    /**

     Test method for {@link Card}.
     The method tests the functionality of saving a Card instance to the database, and then retrieving it to assert if the values are still the same.
     */
    @Test
    void testCard() {
        //arrange
        final int ONE_HUNDRET = 100;
        final int TWO_HUNDRET = 200;
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        card.setCounterFalse(ONE_HUNDRET);
        card.setCounterRight(TWO_HUNDRET);
        final LocalDateTime date = LocalDateTime.now();
        card.setAnsweredLastTime(date);

        userRepository.save(card.getCategory().getOwner());
        categoryRepository.save(card.getCategory());

        //act
        cardRepository.save(card);

        //assert
        List<Card> cards = cardRepository.findCardByCategory(card.getCategory());
        assertEquals(1, cards.size());


        assertEquals(card.getCategory().getCategoryName(), cards.get(0).getCategory().getCategoryName());
        assertEquals(card.getCounterRight(), cards.get(0).getCounterRight());
        assertEquals(card.getCounterFalse(), cards.get(0).getCounterFalse());
        assertEquals(card.getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES), cards.get(0).getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(card.getAnswer(), cards.get(0).getAnswer());
        assertEquals(card.getQuestion(), cards.get(0).getQuestion());
    }

    /**
     * Returns true if the given category is not in the list of categories.
     *
     * @param categories the list of categories to check
     * @param deletedCategory the category name to check if it is not in the list
     * @return true if the category name is not in the list of categories, false otherwise.
     */

    private boolean isNotInList(List<Category> categories, String deletedCategory) {
        for (Category c : categories) {
            if (c.getCategoryName().equals(deletedCategory)) {
                return false;
            }
        }
        return true;
    }


    /**

     Counts the number of categories with names "DEUTSCH", "GESCHICHTE", and "INFORMATIK".
     @param categories the list of categories
     @return the number of categories with the specified names
     */
    private int countRightCategoryNames(List<Category> categories) {

        int counter = 0;
        for (Category c : categories) {

            switch (c.getCategoryName()) {
                case DEUTSCH -> counter++;
                case GESCHICHTE -> counter++;
                case INFORMATIK -> counter++;
                default -> {
                }
            }
        }
        return counter;
    }
}
