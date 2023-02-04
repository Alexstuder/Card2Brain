package ch.zhaw.card2brain.model;

import ch.zhaw.card2brain.TestData.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the {@link Category} class.
 */
class CategoryTest {

    private User defaultUser;

    @BeforeEach
    public void getUser() {

        this.defaultUser = TestDataGenerator.GET_DEFAULT_USER();

    }

    /**
     * Test method for {@link Category#getCategoryName()}.
     * Tests that the method returns the expected category name.
     */
    @Test
    void getCategoryName() {
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        assertEquals(TestDataGenerator.DEFAULT_CATEGORY1, category.getCategoryName());

    }
}
