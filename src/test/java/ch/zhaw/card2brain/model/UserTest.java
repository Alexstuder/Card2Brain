package ch.zhaw.card2brain.model;

import ch.zhaw.card2brain.TestData.TestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test class for the {@link User} class.
 */


class UserTest {


    /**
     * Test method for {@link User#getUserName()}.
     * Tests that the method returns the expected user name.
     */
    @Test
    void getName() {
        User testUser = TestDataGenerator.GET_DEFAULT_USER();
        assertEquals(TestDataGenerator.DEFAULT_USER_NAME, testUser.getUserName());
    }

    /**
     * Test method for {@link User#setUserName(String)}.
     * Tests that the setUserName method correctly sets the user name.
     */
    @Test
    void setName() {

        final String USER_NAME = "User2";
        User testUser = new User();
        testUser.setUserName("User2");
        assertEquals(testUser.getUserName(), USER_NAME);
    }
}
