package ch.zhaw.card2brain.repository;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test class for {@link UserRepository}
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @since 16.01.2022
 */
@SpringBootTest
class UserRepositoryTest extends EmptyDb {

    /**
     * Setting up test data before executing each test case
     */
    @BeforeEach
    void setUp() {
        User nobody = new User("Nobody", "no", "e@mail.com");
        nobody.setPassword("Password");
        userRepository.save(nobody);
    }

    /**
     * Test case for the {@link UserRepository# save(User)} method
     * Verifies that a user can be saved and retrieved by username
     */
    @Test
    public void saveUser() {
        User nobody = new User("NoName", "empty", "no@mail.com");
        nobody.setPassword("Password");
        userRepository.save(nobody);
        assertThat(userRepository.getUserByUserName("Nobody")).isNotNull();
    }

    /**
     * Test case for the {@link UserRepository#getUserByMailAddress(String)} method
     * Verifies that a user can be retrieved by mail address
     */
    @Test
    void getUserByMailAddress() {
        assertEquals(userRepository.getUserByMailAddress("e@mail.com").getUserName(), "Nobody");
    }

    /**
     * Test case for the {@link UserRepository#findAll()} method
     * Verifies that all users can be retrieved
     */
    @Test
    void findAll() {
        List<User> allUsers = userRepository.findAll();
        assertEquals(allUsers.size(), 1);
    }

    /**
     * Test case for the {@link UserRepository#getUserByUserName(String)} method
     * Verifies that a user can be retrieved by username
     */
    @Test
    void getUserByUserName() {
        assertEquals(userRepository.getUserByUserName("Nobody").getMailAddress(), "e@mail.com");
    }
}
