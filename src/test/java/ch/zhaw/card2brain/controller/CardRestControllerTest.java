package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.GenerateTestuserWithToken;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.objectmapper.CardMapper;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for {@link CardRestController}.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * SpringBootTest - marks the test class as a Spring Boot test
 * AutoConfigureMockMvc - used to auto-configure the mockMvc bean
 * @since 16.01.2023
 */
@SpringBootTest
@AutoConfigureMockMvc
class CardRestControllerTest extends GenerateTestuserWithToken {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CardMapper cardMapper;


    /**
     * Test method to test the add card API endpoint.
     *
     * @throws Exception - if any exception occurs while executing the test case
     */

    @Test
    public void testAddCard() throws Exception {

        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientEntity(card.getCategory().getOwner(), card.getCategory());

        CardDto fromBackend = cardMapper.mapToCardDto(card);
        String json = objectMapper.writeValueAsString(fromBackend);


        //act
        MvcResult mvcResult = mockMvc.perform(post("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andReturn();
        //assert
        String jsonRespons = mvcResult.getResponse().getContentAsString();


        CardDto responsCard = objectMapper.readValue(jsonRespons, CardDto.class);
        List<Card> cards = cardRepository.findCardByCategory_Id(card.getCategory().getId());

        // Expected to have just one card added on db
        assertEquals(1, cards.size());
        CardDto expected = cardMapper.mapToCardDto(cards.get(0));
        Assertions.assertTrue(TestDataCompare.COMPARE_CARD_DTO(expected, responsCard));
    }

    /**
     * Test method to test the add card API endpoint, but card already exists.
     *
     * @throws Exception - if any exception occurs while executing the test case
     */
    @Test
    public void testAddCardButAlreadyExists() throws Exception {

        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientEntity(card.getCategory().getOwner(), card.getCategory());
        cardRepository.save(card);

        CardDto fromBackend = cardMapper.mapToCardDto(card);
        String json = objectMapper.writeValueAsString(fromBackend);

        //act
        MvcResult mvcResult = mockMvc.perform(post("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isConflict()).andReturn();
        //assert
        assertEquals("Card exists Already", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());

    }


    /**
     * Test method for adding a card when the corresponding category doesn't exist.
     *
     * @throws Exception if error occurs during the test
     */
    @Test
    public void testAddCardButCategoryDoesntExists() throws Exception {

        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientEntity(card.getCategory().getOwner(), card.getCategory());
        card = cardRepository.save(card);

        // remove all entities from DB
        cardRepository.delete(card);
        categoryRepository.delete(card.getCategory());
        card.setId(null); // add a new card

        CardDto fromBackend = cardMapper.mapToCardDto(card);
        String json = objectMapper.writeValueAsString(fromBackend);

        //act
        MvcResult mvcResult = mockMvc.perform(post("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andReturn();
        //assert
        assertTrue(mvcResult.getResolvedException().getMessage().contains("Category with categoryId :"));
        assertTrue(mvcResult.getResolvedException().getMessage().contains(" not found."));

    }

    /**
     * Test method for {@link {ch.zhaw.card2brain.controller.CardRestController#testUpdateCard()} ()}
     * This test case tests the functionality of updating a card.
     *
     * @throws Exception if an error occurs during the process
     */

    @Test
    public void testUpdateCard() throws Exception {
        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientEntity(card.getCategory().getOwner(), card.getCategory());
        card = cardRepository.save(card);

        // mutation an answer & question
        String fromBackendJson = objectMapper.writeValueAsString(cardMapper.mapToCardDto(getMutation(card)));

        //act
        MvcResult mvcResult = mockMvc.perform(put("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(fromBackendJson)).andExpect(status().isCreated()).andReturn();
        //assert
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        CardDto cardResponse = objectMapper.readValue(jsonResponse, CardDto.class);
        Card cardFromDb = cardRepository.findCardById(card.getId());
        CardDto expected = cardMapper.mapToCardDto(cardFromDb);
        //assert
        Assertions.assertTrue(TestDataCompare.COMPARE_CARD_DTO(expected, cardResponse));
    }


    /**
     * Tests the scenario when a card update is performed but the card does not exist.
     *
     * @throws Exception when there is an error during the test scenario
     */

    @Test
    public void testUpdateCardButCardDoesNotExists() throws Exception {
        //arrange
        Card card = TestDataGenerator.GET_DEFAULT_CARD();
        saveTransientEntity(card.getCategory().getOwner(), card.getCategory());
        card = cardRepository.save(card);
        cardRepository.delete(card);

        // mutation an answer & question
        String fromBackendJson = objectMapper.writeValueAsString(cardMapper.mapToCardDto(getMutation(card)));

        //act
        MvcResult mvcResult = mockMvc.perform(put("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(fromBackendJson)).andExpect(status().isNotFound()).andReturn();
        //assert
        assertEquals("Card does not exist", mvcResult.getResolvedException().getMessage());
    }


    /**
     * Test to retrieve all cards from a category
     *
     * @throws Exception if any error occurs during testing
     */
    @Test
    public void testGetCardsByCategory() throws Exception {
        //arrange
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        List<Card> cards = TestDataGenerator.GET_TEST_CARDS(category);
        saveTransientEntity(category.getOwner(), category);
        cardRepository.saveAll(cards);

        //act
        MvcResult mvcResult = mockMvc.perform(get("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).param("categoryId", category.getId().toString())).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        TypeReference<List<CardDto>> mapType = new TypeReference<List<CardDto>>() {
        };
        List<CardDto> cardDtosResponse = objectMapper.readValue(jsonResponse, mapType);


        //assert
        List<CardDto> expectedDtos = cardRepository.findCardByCategory_Id(category.getId()).stream().map((Card card) -> cardMapper.mapToCardDto(card)).collect(Collectors.toList());
        assertEquals(expectedDtos.size(), cardDtosResponse.size(), "Expected size : " + expectedDtos.size() + " doesn't match actual size: " + cardDtosResponse.size());
        Assertions.assertTrue(TestDataCompare.COMPARE_CARD_DTO_LISTS(expectedDtos, cardDtosResponse), "Expected cardDto list is different from response cardDto list");

    }

    /**
     * Test for get cards by category API endpoint when category does not exist in the database.
     *
     * @throws Exception if the operation fails
     */

    @Test
    public void testGetCardsByCategoryButCategoryDoesNotExist() throws Exception {
        //arrange

        //act
        MvcResult mvcResult = mockMvc.perform(get("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).param("categoryId", "123456789")).andExpect(status().isNotFound()).andReturn();

        //assert
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertTrue(mvcResult.getResolvedException().getMessage().contains("Category with categoryId :123456789 not found."));

    }

    /**
     * Test class for {@see ch.zhaw.card2brain.controller.CardController}'s DELETE functionality.
     * <li>Deleting a card by ID</li>
     *
     * @throws Exception if the operation fails
     */

    @Test
    public void testDeleteCardByID() throws Exception {
        //arrange
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        List<Card> cards = TestDataGenerator.GET_TEST_CARDS(category);
        int expectedCardsRestOnDb = cards.size() - 1;
        saveTransientEntity(category.getOwner(), category);
        cardRepository.saveAll(cards);

        // get Random Id to delete
        long cardToDelete = cards.get(27).getId();
        //act
        MvcResult mvcResult = mockMvc.perform(delete("/api/cards/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).param("cardId", String.valueOf(cardToDelete))).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        //assert
        //card doesnt existst on DB
        Assertions.assertNull(cardRepository.findCardById(cardToDelete));
        List<CardDto> expected = cardRepository.findCardByCategory_Id(category.getId()).stream().map((Card card) -> cardMapper.mapToCardDto(card)).collect(Collectors.toList());
        assertEquals(expectedCardsRestOnDb, expected.size());
        assertEquals("", jsonResponse);

    }

    /**
     * Creates a new card with mutation values from a given card.
     *
     * @param card The original card from which the mutation values are taken.
     */

    private Card getMutation(Card card) {
        Card mutation = new Card();
        mutation.setId(card.getId());
        mutation.setCategory(card.getCategory());
        mutation.setQuestion("Question mutation");
        mutation.setCounterFalse(card.getCounterFalse());
        mutation.setCounterRight(card.getCounterRight());
        mutation.setCorrectAnswersInRow(card.getCorrectAnswersInRow());
        mutation.setNextDateToRepeat(card.getNextDateToRepeat());
        mutation.setAnsweredLastTime(card.getAnsweredLastTime());
        mutation.setAnswer("Answer mutation");
        return mutation;
    }

    /**
     * Saves transient entities.
     *
     * @param user     the user entity to be saved
     * @param category the category entity to be saved
     */

    private void saveTransientEntity(User user, Category category) {
        userRepository.save(user);
        categoryRepository.save(category);
    }

}
