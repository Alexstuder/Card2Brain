package ch.zhaw.card2brain.model;

import ch.zhaw.card2brain.TestData.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit test for the {@link Card} class.
 */
class CardTest {

    private Card testCard;


    @BeforeEach
    public void init() {
        this.testCard = TestDataGenerator.GET_DEFAULT_CARD();
    }

    /**
     * Tests the getter methods of the {@link Card} class.
     */
    @Test
    void getCard() {

        assertEquals(TestDataGenerator.ANSWER, testCard.getAnswer());
        assertEquals(TestDataGenerator.QUESTION, testCard.getQuestion());
        assertEquals(TestDataGenerator.DEFAULT_CATEGORY1, testCard.getCategory().getCategoryName());

    }

}
