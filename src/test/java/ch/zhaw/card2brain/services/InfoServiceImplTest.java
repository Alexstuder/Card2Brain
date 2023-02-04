package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.dto.InfoDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link InfoServiceImpl}
 *  * @author Niklaus Hänggi
 *  * @author Alexander Studer
 *  * @author Roman Joller
 *  * @version 1.0
 *  * @since 28-01-2023
 */
@SpringBootTest
class InfoServiceImplTest extends EmptyDb {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    private InfoService infoService;

    /**
     * Test method to test {@link InfoServiceImpl#getInfos(User)}
     * This method tests if the correct InfoDto objects are returned for a given User.
     *
     * It saves a user, categories, and cards in the database, then it invokes the
     * getInfos method and asserts that the returned InfoDto objects match the expected
     * values.
     */
    @Test
    void testGetInfos() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setPassword("Password");
        User userReturn = userRepository.save(user);


        final String DEUTSCH = "Deutsch";
        final String LATEIN = "Latein";
        final String NO_CARDS_TO_LEARN = "No Cards to learn";

        Category deutsch = new Category(DEUTSCH, user);
        Category latein = new Category(LATEIN, user);
        Category withOutCardsToRepeate = new Category(NO_CARDS_TO_LEARN, user);

        categoryRepository.save(deutsch);
        categoryRepository.save(latein);
        categoryRepository.save(withOutCardsToRepeate);

        int deutschCards = 140;
        int deutschToLearn = 77;
        cardRepository.saveAll(getCards(deutsch, deutschToLearn, deutschCards));

        int lateinCards = 250;
        int lateinToLearn = 14;
        cardRepository.saveAll(getCards(latein, lateinToLearn, lateinCards));

        int noCardsToLearnCards = 250;
        int noCardsToLearnToLearn = 0;
        cardRepository.saveAll(getCards(withOutCardsToRepeate, noCardsToLearnToLearn, noCardsToLearnCards));

        List<InfoDto> infoDtos = infoService.getInfos(user);

        //assert
        assertTrue(infoDtos.get(0).getCategoryName().contentEquals(DEUTSCH));
        assertEquals(infoDtos.get(0).getNumberOfCards(), deutschCards);
        assertEquals(infoDtos.get(0).getToLearn(), deutschToLearn);

        assertTrue(infoDtos.get(1).getCategoryName().contentEquals(LATEIN));
        assertEquals(infoDtos.get(1).getNumberOfCards(), lateinCards);
        assertEquals(infoDtos.get(1).getToLearn(), lateinToLearn);

        assertTrue(infoDtos.get(2).getCategoryName().contentEquals(NO_CARDS_TO_LEARN));
        assertEquals(infoDtos.get(2).getNumberOfCards(), noCardsToLearnCards);
        assertEquals(infoDtos.get(2).getToLearn(), noCardsToLearnToLearn);

    }

    /**
     * Helper method to generate a list of cards for a given category, number of cards to repeat,
     * and total number of cards.
     *
     * @param category the category for which the cards belong to
     * @param toRepeat the number of cards to repeat
     * @param total the total number of cards
     * @return the list of cards
     */


    private List<Card> getCards(Category category, int toRepeat, int total) {
        List<Card> cards = new ArrayList<>();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Integer> randomId = getRandomId(toRepeat, total);
        int indexOfRandomId = 0;

        for (int i = 0; i < total; i++) {
            Card card = new Card("Qustion", "Answer", category);

            //get a list of non to repeat cards,cuz next dy will start tomorrow
            card.setNextDateToRepeat(tomorrow.plusDays(i));
            if (indexOfRandomId < randomId.size() && i == randomId.get(indexOfRandomId)) {
                card.setNextDateToRepeat(LocalDate.now().minusDays(i));
                indexOfRandomId++;
            }
            cards.add(card);
        }
        return cards;
    }

    /**
     * Helper method to generate a list of random ids for the cards to repeat.
     *
     * @param toRepeat the number of cards to repeat
     * @param total the total number of cards
     * @return the list of random ids
     */

    private List<Integer> getRandomId(int toRepeat, int total) {
        TreeSet<Integer> randomCardToRepeatId = new TreeSet<>();
        while (randomCardToRepeatId.size() < toRepeat) {
            randomCardToRepeatId.add(new Random().nextInt(total));
        }
        return randomCardToRepeatId.stream().toList();
    }
}
