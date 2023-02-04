package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.services.CardService;
import ch.zhaw.card2brain.services.CategoryService;
import ch.zhaw.card2brain.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link CardMapper}
 * Converts CardDto to Card / Card to CardDto / Id to Card
 * Extends {@link EmptyDb} to run the tests on an empty database
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */


@SpringBootTest
class CardMapperTest extends EmptyDb {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;

    long categoryId;
    Category category;
    @Autowired
    CardMapper cardMapper;


    @BeforeEach
    void setUp() {
        category = new Category("Test Category", new User("nik", "niklaus", "nik@mail.com"));
        User user = category.getOwner();
        user.setPassword("Password");
        userService.addUser(user);
        Category category1 = categoryService.createCategory(category);
        categoryId = category1.getId();


    }

    /**
     * Test method for {@link CardMapper#mapToCardDto(Card)}
     * creates a test card object and maps it to cardDto by calling the method
     * verifies that the resulting cardDto object is as expected
     */

    @Test
    public void testMapToCardDto() {
        // Set up a test Card object
        Card card = new Card();
        card.setId(1L);
        card.setQuestion("What is the capital of France?");
        card.setAnswer("Paris");
        Category category = new Category();
        category.setId(2L);
        card.setCategory(category);

        // Call the method under test
        CardDto cardDto = cardMapper.mapToCardDto(card);

        // Verify the resulting CardDto object is as expected
        assertEquals(1L, cardDto.getId());
        assertEquals("What is the capital of France?", cardDto.getQuestion());
        assertEquals("Paris", cardDto.getAnswer());
        assertEquals(2L, cardDto.getCategoryId());
    }


    /**
     * Test method for {@link CardMapper#mapToCard(CardDto)}
     * creates a mock category object and a test cardDto object
     * maps the cardDto object to card by calling the method
     * verifies that the resulting card object is as expected
     */

    @Test
    public void testMapToCard() {

        // Set up a mock Category object
        Category category = new Category();
        category.setId(2L);

        // Set up a test CardDto object
        CardDto cardDto = new CardDto();
        cardDto.setId(1L);
        cardDto.setQuestion("What is the capital of France?");
        cardDto.setAnswer("Paris");
        cardDto.setCategoryId(categoryId);


/**
 Test for {@link CardMapper#mapFromIdToCard(long)}
 This test verifies that the method correctly maps a card ID to a {@link Card} object
 by calling the {@link CardService#getCard(long)} method and {@link CategoryService#getCategoryById(long)} method
 and returns the correct {@link Card} object
 */


        // Call the method under test
        Card card = cardMapper.mapToCard(cardDto);

        // Verify the resulting Card object is as expected
        assertEquals(1L, card.getId());
        assertEquals("What is the capital of France?", card.getQuestion());
        assertEquals("Paris", card.getAnswer());
        assertEquals(categoryId, card.getCategory().getId());
    }


    /**
     * Test method for {@link CardMapper#mapFromIdToCard(Long)}.
     * Tests that the method correctly maps from the card's ID to the card object.
     */

    @Test
    public void testMapFromIdToCard() {
        //Arrange

        Card expectedCard = new Card("Question", "Answer", category);
        Card card = cardService.addCard(expectedCard);
        Long cardId = card.getId();


        //Act
        Card result = cardMapper.mapFromIdToCard(cardId);
        //Assert
        assertEquals(expectedCard.getId(), result.getId());

    }
}
