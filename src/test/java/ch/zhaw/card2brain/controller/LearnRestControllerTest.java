package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.GenerateTestuserWithToken;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.dto.LearnDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.services.CardService;
import ch.zhaw.card2brain.services.RepetitionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class LearnRestControllerTest extends GenerateTestuserWithToken {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    CardService cardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RepetitionService repetitionService;

    private final static String CARD_TO_REPEAT = "Card to repeat";

    private Card card;
    private User user;

    @BeforeEach
    void setUp() {
        user = TestDataGenerator.GET_DEFAULT_USER();
        Category category = TestDataGenerator.GET_TEST_CATEGORY("Mathematik", user);
        card = TestDataGenerator.GET_TEST_CARD(category);

        user = userRepository.save(user);
        category = categoryRepository.save(category);
        card = cardRepository.save(card);
    }

    /**
     * Test method to verify that the answer provided by a user during a learning session was correct.
     *
     * @throws Exception if the request to the endpoint fails
     */
    @Test
    void cardLearnedAnswerWasCorrect() throws Exception {

        //arrange
        LearnDto learnDto = new LearnDto(card.getId(), true);

        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(learnDto))).andExpect(status().isOk()).andReturn();

        //assert
        String jsonRespons = mvcResult.getResponse().getContentAsString();
        Card expectedCard = cardRepository.findCardById(card.getId());

        assertEquals("", jsonRespons);
        assertEquals(card.getCorrectAnswersInRow() + 1, expectedCard.getCorrectAnswersInRow());
        assertEquals(card.getCounterFalse(), expectedCard.getCounterFalse());
        assertEquals(card.getCounterRight() + 1, expectedCard.getCounterRight());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), expectedCard.getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(card.getAnswer(), expectedCard.getAnswer());
        assertEquals(card.getQuestion(), expectedCard.getQuestion());
        assertTrue(TestDataCompare.COMPARE_CATEGORY(expectedCard.getCategory(), card.getCategory()));
        assertEquals(repetitionService.getNextRepetitionDate(card.getCorrectAnswersInRow() + 1), expectedCard.getNextDateToRepeat());

    }

    /**
     * Test method to check if a card learned answer is saved with Not Found response when the card is not found.
     * It tests if the API returns a 404 Not Found status code when the provided card id does not match any existing card.
     *
     * @throws Exception when an error occurs during the process
     */
    @Test
    void cardLearnedAnswerButNoCardFound() throws Exception {
        //arrange
        LearnDto learnDto = new LearnDto(card.getId(), true);
        cardRepository.delete(card);
        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(learnDto))).andExpect(status().isNotFound()).andReturn();

        //assert
        assertTrue(Objects.requireNonNull(mvcResult.getResolvedException()).getMessage().contains("Card does not exist"));
    }


    /**
     * Test for successful learning when the answer provided is incorrect.
     *
     * @throws Exception if any error occurs during the test
     */


    @Test
    void cardLearnedAnswerWasNotCorrect() throws Exception {
        //arrange
        LearnDto learnDto = new LearnDto(card.getId(), false);

        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(learnDto))).andExpect(status().isOk()).andReturn();

        //assert
        String jsonRespons = mvcResult.getResponse().getContentAsString();
        Card expectedCard = cardRepository.findCardById(card.getId());

        assertEquals("", jsonRespons);
        assertEquals(0, expectedCard.getCorrectAnswersInRow());
        assertEquals(card.getCounterFalse() + 1, expectedCard.getCounterFalse());
        assertEquals(card.getCounterRight(), expectedCard.getCounterRight());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), expectedCard.getAnsweredLastTime().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(card.getAnswer(), expectedCard.getAnswer());
        assertEquals(card.getQuestion(), expectedCard.getQuestion());
        assertTrue(TestDataCompare.COMPARE_CATEGORY(expectedCard.getCategory(), card.getCategory()));
        assertEquals(repetitionService.getNextRepetitionDate(expectedCard.getCorrectAnswersInRow()), expectedCard.getNextDateToRepeat());

    }


    /**
     * Test method to verify that when trying to get all cards to learn for a non-existing category,
     * a "not found" error is returned with a corresponding error message.
     *
     * @throws Exception if the test encounters an error
     */
    @Test
    void getAllCardsToLearnButCategorieDoesNotExist() throws Exception {
        //arrange
        Long categoryIdNotExist = 4711L;

        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).param("categoryId", String.valueOf(categoryIdNotExist))).andExpect(status().isNotFound()).andReturn();

        //assert
        assertEquals("Category with categoryId :" + categoryIdNotExist + " not found.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
    }

    /**
     * Test for the method getAllCardsToLearn() in the LearnController.
     * The test will check if the response is "Ok" and contains the expected number of cards to learn.
     * Additionally, the test will check if the returned cards match the expected cards.
     *
     * @throws Exception in case an error occurs during the request/response process
     */
    @Test
    void getAllCardsToLearn() throws Exception {
        //arrange
        Category categoryToRepeat = TestDataGenerator.GET_TEST_CATEGORY("CategoryToRepeat", user);
        categoryRepository.save(categoryToRepeat);
        List<Card> cardList = TestDataGenerator.GET_TEST_CARDS(categoryToRepeat);
        cardRepository.saveAll(arrangeDateForCardList(cardList));

        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).param("categoryId", String.valueOf(categoryToRepeat.getId()))).andExpect(status().isOk()).andReturn();

        TypeReference<List<CardDto>> mapType = new TypeReference<>() {
        };
        List<CardDto> cardDtosResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), mapType);
        //assert
        assertEquals(25, cardDtosResponse.size());
        assertTrue((containsTheRightCard(cardDtosResponse)));

    }

    /**
     * Test method to get all the cards to learn in a specific category.
     * In this test, there are no cards to learn.
     *
     * @throws Exception in case of a failure in the test
     */
    @Test
    void getAllCardsButNoCardsToLearn() throws Exception {
        //arrange
        Category categoryToRepeat = TestDataGenerator.GET_TEST_CATEGORY("CategoryToRepeat", user);
        categoryRepository.save(categoryToRepeat);
        List<Card> cardList = TestDataGenerator.GET_TEST_CARDS(categoryToRepeat);
        cardRepository.saveAll(arrangeDateNoCardsToLearnList(cardList));

        //test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/learns/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).param("categoryId", String.valueOf(categoryToRepeat.getId()))).andExpect(status().isOk()).andReturn();

        TypeReference<List<CardDto>> mapType = new TypeReference<>() {
        };
        List<CardDto> cardDtosResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), mapType);
        //assert
        assertEquals(0, cardDtosResponse.size());

    }

    /**
     * Helper method to set the next date to repeat for a list of cards to a future date so that none of the cards will be selected for repeat.
     *
     * @param cardList the list of cards
     * @return the updated list of cards with future next date to repeat
     */

    private List<Card> arrangeDateNoCardsToLearnList(List<Card> cardList) {

        int i = 1;
        // Set DateTo Repeat to Future so ,none Card will be selected to repeat
        for (Card card : cardList) {
            card.setNextDateToRepeat(LocalDate.now().plusDays(i++));
        }

        return cardList;
    }

    /**
     * Verifies if the given list of {@link CardDto} contains the right cards.
     *
     * @param cardDtosResponse the list of {@link CardDto} to check
     * @return true if all the cards in the list have the right answer, false otherwise
     */

    private boolean containsTheRightCard(List<CardDto> cardDtosResponse) {

        for (CardDto cardDto : cardDtosResponse) {
            if (!cardDto.getAnswer().equals(CARD_TO_REPEAT)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Arranges the dates for the given list of cards.
     * The next date to repeat is set to half of the list size behind the current date.
     * The answer is set to `CARD_TO_REPEAT` if the next date to repeat is today or in the past.
     * The answer is set to `"Card not to repeat"` otherwise.
     *
     * @param cardList the list of cards to arrange dates for
     * @return the arranged list of cards
     */

    private List<Card> arrangeDateForCardList(List<Card> cardList) {
        LocalDate actualDate = LocalDate.now();
        int halfSize = cardList.size() / 2 * -1;

        for (Card card : cardList) {
            card.setNextDateToRepeat(actualDate.plusDays(++halfSize));
            if (card.getNextDateToRepeat().isBefore(LocalDate.now()) || card.getNextDateToRepeat().isEqual(LocalDate.now())) {
                card.setAnswer(CARD_TO_REPEAT);
            } else {
                card.setAnswer("Card not to repeat");
            }
        }
        return cardList;
    }

}



