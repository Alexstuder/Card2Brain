package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.exception.CardNotValidException;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class Card2BrainDbCardServiceTests provides JUnit tests for Card2BrainDbCardService.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 30-01-2023
 */
@SpringBootTest
class Card2BrainDbCardServiceTests extends EmptyDb {

    private static final String MUTATION = "Mutation";
    @Autowired
    CardService cardService;

    @Autowired
    RepetitionService repetitionService;
    private User user;
    private Category category;
    @Autowired
    private UserRepository userRepository;

    /**
     * Test data generator method that returns a stream of arguments to be used in testing.
     * Each argument contains a card instance with either an empty question or answer, and a corresponding error message.
     * The error message is used to assert the result of the card validation.
     *
     * @return a stream of card instances and error messages
     */

    private static Stream<Arguments> readNotValidCards() {
        return Stream.of(Arguments.of(new Card("Question", "", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Card is invalid. Please enter an answer."), Arguments.of(new Card("Question", " ", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Card is invalid. Please enter an answer."), Arguments.of(new Card("", "", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Card is invalid. Please enter an answer."), Arguments.of(new Card("", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Card is invalid. Please enter a question."), Arguments.of(new Card(" ", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Card is invalid. Please enter a question."));
    }

    /**
     * @return a {@link Stream} of {@link Arguments} representing not valid Cards for update.
     */

    private static Stream<Arguments> readNotValidCardsForUpdate() {
        return Stream.of(Arguments.of(new Card("Question", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Question", "", "Card is invalid. Please enter an answer."), Arguments.of(new Card("Question", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "Question", " ", "Card is invalid. Please enter an answer."), Arguments.of(new Card("Question", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "", "", "Card is invalid. Please enter an answer."), Arguments.of(new Card("Question", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), "", "Answer", "Card is invalid. Please enter a question."), Arguments.of(new Card("Question", "Answer", new Category("anyCategory", new User("any", "any", "a@a.a"))), " ", "Answer", "Card is invalid. Please enter a question."));
    }

    /**
     * Tests getCardsForRepetition() method.
     *
     * @throws Exception
     */
    @Test
    public void testGetCardsForRepetition() {
        //arrange
        // get Dates
        LocalDate actualDate = LocalDate.now();

        //prepare Cards On DB
        User user = TestDataGenerator.GET_DEFAULT_USER();
        Category categoryToRepeat = new Category("RepetitionCategory", user);
        //prepare Cards On DB

        Category categoryNotToRepeat = new Category("NotToRepeatCategory", user);


        Card cardWithZeroRightAnswer = new Card("QuestionToRepeat", "Answer", categoryToRepeat);
        cardWithZeroRightAnswer.setCorrectAnswersInRow(0);

        Card cardToRepeatToday = new Card("QuestionToRepeat", "Answer", categoryToRepeat);
        cardToRepeatToday.setNextDateToRepeat(actualDate);

        Card cardToRepeatTomorrow = new Card("QuestionNotToRepeat", "Answer", categoryToRepeat);
        cardToRepeatTomorrow.setNextDateToRepeat(actualDate.plusDays(1));

        Card cardRepeatInAnotherCategory = new Card("QuestionNotToRepeat", "Answer", categoryNotToRepeat);
        cardRepeatInAnotherCategory.setCorrectAnswersInRow(0);
        cardRepeatInAnotherCategory.setNextDateToRepeat(actualDate);

        userRepository.save(user);
        categoryRepository.save(categoryToRepeat);
        categoryRepository.save(categoryNotToRepeat);
        cardRepository.save(cardWithZeroRightAnswer);
        cardRepository.save(cardToRepeatToday);
        cardRepository.save(cardToRepeatTomorrow);
        cardRepository.save(cardRepeatInAnotherCategory);
        //act
        List<Card> resultCards = cardService.getCardsForRepetition(categoryToRepeat);

        //assert
        assertEquals(2, resultCards.size());
    }

    /**
     * Tests getCardsForRepetition() method for user without any cards to repeat.
     *
     * @throws Exception
     */
    @Test
    public void testGetCardsForRepetitionWithUserWithoutAnyCardsToRepeat() {
        //arrange
        Category categoryWithoutCardsToRepeate = TestDataGenerator.GET_DEFAULT_CATEGORY();
        userRepository.save(categoryWithoutCardsToRepeate.getOwner());
        categoryRepository.save(categoryWithoutCardsToRepeate);
        //act
        List<Card> cards = cardService.getCardsForRepetition(categoryWithoutCardsToRepeate);

        //assert
        assertEquals(0, cards.size());

    }

    /**
     * Test method for {@link CardService#repeated(Card, boolean)}.
     * This method tests the repeated functionality of the CardService class.
     * It verifies that the right answer counter, false answer counter, and the number of correct answers in a row are updated correctly
     * and the next repetition date is correctly set when a card's answer is recorded as false.
     */

    @Test
    public void testRepeatedCorrectAnswer() {


        //arrange
        final int RIGHT_ANSWER_IN_ROW = 5;
        final int RIGHT_ANSWER = 10;
        final int FALSE_ANSWER = 20;
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        card.setCorrectAnswersInRow(RIGHT_ANSWER_IN_ROW);
        card.setCounterRight(RIGHT_ANSWER);
        card.setCounterFalse(FALSE_ANSWER);
        userRepository.save(card.getCategory().getOwner());
        categoryRepository.save(card.getCategory());
        cardRepository.save(card);

        //act
        cardService.repeated(card, true);

        //assert
        Optional<Card> testee = cardRepository.findById(card.getId());
        assert testee.orElse(null) != null;
        assertEquals(testee.get().getCounterRight(), RIGHT_ANSWER + 1);
        assertEquals(testee.get().getCounterFalse(), FALSE_ANSWER);
        assertEquals(testee.get().getCorrectAnswersInRow(), RIGHT_ANSWER_IN_ROW + 1);
        assertEquals(testee.get().getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(testee.get().getNextDateToRepeat(), repetitionService.getNextRepetitionDate(RIGHT_ANSWER_IN_ROW + 1));
    }

    /**
     * Test for the {@link CardService#repeated(Card, boolean)} method.
     * The method tests that a Card's counters and next repetition date are updated correctly
     * when the repeated answer is incorrect.
     */
    @Test
    public void testRepeatedWrongAnswer() {

        //arrange
        final int RIGHT_ANSWER_IN_ROW = 5;
        final int RIGHT_ANSWER = 10;
        final int FALSE_ANSWER = 20;
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        card.setCorrectAnswersInRow(RIGHT_ANSWER_IN_ROW);
        card.setCounterRight(RIGHT_ANSWER);
        card.setCounterFalse(FALSE_ANSWER);
        userRepository.save(card.getCategory().getOwner());
        categoryRepository.save(card.getCategory());
        cardRepository.save(card);

        //act
        cardService.repeated(card, false);

        //assert
        Optional<Card> testee = cardRepository.findById(card.getId());
        assert testee.orElse(null) != null;
        assertEquals(testee.get().getCounterRight(), RIGHT_ANSWER);
        assertEquals(testee.get().getCounterFalse(), FALSE_ANSWER + 1);
        assertEquals(testee.get().getCorrectAnswersInRow(), 0);
        assertEquals(testee.get().getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(testee.get().getNextDateToRepeat(), repetitionService.getNextRepetitionDate(0));
    }

    /**
     * Test method for {@link CardService#getAllCardsOfACategoryById(long)}.
     * This method tests the retrieval of all cards of a category by category id.
     * It verifies that the correct number of cards is returned and that the user and category name match the expected values.
     */
    @Test
    public void testGetAllCardsOfACategory() {
        //arrange
        //save test Cards on DB
        List<Card> cards = getInitalCards();
        cardRepository.saveAll(cards);
        long categoryId = cards.get(0).getCategory().getId();
        //act
        List<Card> cardsFromDb = cardService.getAllCardsOfACategoryById(categoryId);
        //assert
        assertEquals(50, cardsFromDb.size());
        assertEquals(category.getCategoryName(), cardsFromDb.get(0).getCategory().getCategoryName());
        assertTrue(assertUserIsSame(user, cardsFromDb.get(0).getCategory().getOwner()));
    }

    /**
     * Tests the scenario where a null card is provided to the card validation process.
     * The expected behavior is that a {@link CardNotValidException} is thrown with the message "Card is Null!".
     */
    @Test
    public void testCardIsNull() {
        //arrange

        //act
        CardNotValidException cardNotValidException = assertThrows(CardNotValidException.class, () -> {
            cardService.isCardValid(null);
        });

        //assert
        assertEquals(cardNotValidException.getMessage(), "Card is Null!");
    }

    /**
     * Verifies if two User instances represent the same user.
     *
     * @param user  First User instance to compare
     * @param owner Second User instance to compare
     * @return true if both User instances are equal, false otherwise
     */

    private boolean assertUserIsSame(User user, User owner) {
        return user.getUserName().equals(owner.getUserName()) && (user.getFirstName().equals(owner.getFirstName())) && (user.getMailAddress().equals(owner.getMailAddress()));
    }

    /**
     * This method saves a card and its associated category and user entities to the database.
     *
     * @param card The card to be saved.
     */

    private void saveTransientInstance(Card card) {
        userRepository.save(card.getCategory().getOwner());
        categoryRepository.save(card.getCategory());
    }

    @ParameterizedTest
    @MethodSource("readNotValidCards")
    void testAddCardsButCardIsNotValidNoQuestion(Card card, String errorMsg) {
        //arrange
        saveTransientInstance(card);

        //act
        CardNotValidException cardNotValidException = assertThrows(CardNotValidException.class, () -> {
            cardService.addCard(card);
        });

        //assert
        assertEquals(cardNotValidException.getMessage(), errorMsg);

    }

    /**
     * This is a test case for the {@link CardService#addCard(Card)} method.
     * The test case creates a new {@link Card} object using {@link TestDataGenerator#GET_DEFAULT_CARD()} and saves it to the database by calling the {@link #saveTransientInstance(Card)} method.
     * The {@link CardService#addCard(Card)} method is then called with the newly created card as its parameter.
     * Finally, the test asserts that the returned {@link Card} object from the service call is equal to the original card and that there is exactly one card in the database.
     */
    @Test
    void testAddCards() {
        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientInstance(card);

        //act
        Card cardFromDb = cardService.addCard(card);

        //assert
        List<Card> all = cardRepository.findAll();

        assertEquals(1, all.size());
        assertEquals(card.toString(), cardFromDb.toString());

    }

    /**
     * Tests the method updateCard in the CardService class.
     * <p>The test performs the following steps:
     * <ol>
     * <li>Saves a default test Card in the database</li>
     * <li>Generates mutations for the saved Card</li>
     * <li>Updates the saved Card with the mutations</li>
     * <li>Checks if the updated Card in the database has the expected mutations</li>
     * </ol>
     */

    @Test
    public void testUpdateCards() {

        //arrange
        //save test Cards on DB
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientInstance(card);
        Card cardOnDb = cardRepository.save(card);

        //get some mutations
        Card mutationOnCard = getMutationOnCard(card);

        //act
        cardService.updateCard(mutationOnCard);

        //assert
        List<Card> cardsFromDbAfterMutation = cardRepository.findCardByCategory_Id(mutationOnCard.getCategory().getId());
        assertEquals(1, cardsFromDbAfterMutation.size());
        assertEquals("mutation Answer", cardsFromDbAfterMutation.get(0).getAnswer());
        assertEquals("mutation Question", cardsFromDbAfterMutation.get(0).getQuestion());

    }

    /**
     * This method is used to return a {@link Stream} of {@link Arguments}
     * representing cards which are not valid for update.
     * Each argument is an instance of {@link Card} with predefined question, answer and category.
     * The second and third argument are the question and answer fields to be updated respectively.
     * The fourth argument is the expected error message.
     *
     * @return a stream of arguments
     */
    @ParameterizedTest
    @MethodSource("readNotValidCardsForUpdate")
    public void testUpdateCardsButCardIsNotValid(Card card, String mutQ, String mutA, String errorMsg) {

        //arrange
        //save test Cards on DB
        saveTransientInstance(card);
        Card cardOnDb = cardRepository.save(card);

        //get some mutations
        Card mutationOnCard = getMutationOnCard(card);
        mutationOnCard.setQuestion(mutQ);
        mutationOnCard.setAnswer(mutA);

        //act
        CardNotValidException cardNotValidException = assertThrows(CardNotValidException.class, () -> {
            cardService.updateCard(mutationOnCard);
        });

        //assert
        assertEquals(cardNotValidException.getMessage(), errorMsg);
    }

    /**
     * Returns a mutated version of a {@link Card}.
     * The mutation includes changes to the answer and question fields, but retains the other fields as is.
     *
     * @param card The original {@link Card} to be mutated.
     * @return The mutated version of the {@link Card}.
     */

    private Card getMutationOnCard(Card card) {
        Card mutation = new Card();
        mutation.setId(card.getId());
        mutation.setAnswer("mutation Answer");
        mutation.setQuestion("mutation Question");
        mutation.setAnsweredLastTime(card.getAnsweredLastTime());
        mutation.setNextDateToRepeat(card.getNextDateToRepeat());
        mutation.setCorrectAnswersInRow(card.getCorrectAnswersInRow());
        mutation.setCategory(card.getCategory());
        mutation.setCounterFalse(card.getCounterFalse());
        mutation.setCounterRight(card.getCounterRight());
        return mutation;
    }

    /**
     * Tests the {@link CardService#deleteCard(Card)} method.
     * Arranges the test by creating two cards with the same answer, saving them on the database,
     * then deletes one of the cards and asserts that the card still exists on the database.
     */
    @Test
    public void testDeleteCard() {
        //arrange
        final String ANSWER = "Still Exist";
        //save test Cards on DB
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        card.setAnswer(ANSWER);
        saveTransientInstance(card);
        Card cardToDeleteOnDb = cardRepository.save(card);

        Card card2 = TestDataGenerator.GET_DEFAULT_CARD();
        card2.setAnswer(ANSWER);
        card2.setCategory(card.getCategory());
        Card cardStillExistsOnDb = cardRepository.save(card2);

        //act
        cardService.deleteCard(cardToDeleteOnDb);

        //assert
        List<Card> cards = cardService.getAllCardsOfACategoryById(card.getCategory().getId());
        assertEquals(1, cards.size());
        assertEquals(ANSWER, cards.get(0).getAnswer());

    }


    /**
     * Check if the right cards are still in the database and the rest are deleted.
     *
     * @param cardsToDelete the cards to delete
     * @param cards         the cards in the database
     * @return true if there are 48 cards still in the database and 2 were deleted, false otherwise.
     */
    private boolean rightCardsExistOnDB(List<Card> cardsToDelete, List<Card> cards) {

        int counterExistsOnDb = 0;
        int counterDeleted = 0;
        for (Card card : cards) {
            if (cardRepository.existsById(card.getId())) {
                counterExistsOnDb++;
            } else {
                for (Card deletedCard : cardsToDelete) {
                    if (card.getId().equals(deletedCard.getId())) {
                        counterDeleted++;
                    }
                }
            }
        }
        return counterExistsOnDb == 48 && counterDeleted == 2;
    }


    /**
     * Returns a list of initial cards for testing purposes.
     * The method creates a default user, category and then generates a list of test cards with the created category.
     *
     * @return a list of initial cards for testing purposes.
     */

    private List<Card> getInitalCards() {
        user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);

        category = TestDataGenerator.GET_DEFAULT_CATEGORY_TO_USER(user);
        categoryRepository.save(category);

        return TestDataGenerator.GET_TEST_CARDS(category);

    }
}
