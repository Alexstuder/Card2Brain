package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.exception.*;
import ch.zhaw.card2brain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class contains JUnit 5 tests for the {@link UserService} class.
 * The tests include various scenarios such as adding a user, updating a user,
 * and deleting a user, as well as testing for exceptions such as a user not being found
 * or an email address already being taken.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */

@SpringBootTest
class Card2BrainDbUserServiceTests extends EmptyDb {
    @Autowired
    UserService userService;

    /**
     * This test method tests the successful addition of a user to the database.
     * It creates a user, adds it to the database, and checks that the id returned
     * is greater than or equal to 0.
     */


    @Test
    void testUserServiceUserAdded() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setPassword("Password");
        User userReturn = userService.addUser(user);


        //assert
        assertTrue(userReturn.getId() >= 0);

    }


    /**
     * Test method for {@link UserService#findAll()}.
     * This test method tests the successful addition of 7 users to the database and asserts that the number of users in the database is correct.
     * The test creates 7 UserDto objects and adds them to the database using the addUser method of the UserService.
     * Then the findAll method of the UserService is used to retrieve all the users from the database and the number of users is compared with the expected value.
     */


    @Test
    void testAddSevenUsers() {
        //arrange
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            User user = new User("USERNAME" + i, "FIRSTNAME" + i, "name" + i + "@domain.com");
            user.setPassword("Password");
            users.add(user);
            userService.addUser(user);
        }

        //act
        List<User> allUsers = userService.findAll();

        //assert
        assertEquals(7, allUsers.size());
        for (int i = 0; i < 7; i++) {
            assertEquals("FIRSTNAME" + i, allUsers.get(i).getFirstName());

        }

    }

    /**
     * Test method that tests the successful update of a user.
     * This test method tests the following scenario:
     * - Arrange: Create a new user and add it to the database.
     * - Act: Update the user's username and call the update method on the userService.
     * - Assert: Check if the username of the returned user after update is equal to the expected new name.
     */


    @Test
    void testUpdateUserSuccess() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setPassword("Password");
        User addedUser = userService.addUser(user);
        addedUser.setUserName("newName");

        //act
        User updatedUser = userService.updateUser(addedUser);

        //assert
        assertEquals("newName", updatedUser.getUserName());
    }

    /**
     * This test method tests the case where the user is not found when trying to update it in the database.
     * It does this by first creating a user object, converting it to a User, and then attempting to update it in the database.
     * Since the user does not exist in the database, a UserNotFoundException should be thrown. The test asserts that the exception message is as expected.
     */

    @Test
    void testUpdateUserUserNotFound() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setPassword("Password");

        //act
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("User " + user.getUserName() + " does not exist.", exception.getMessage());
    }

    /**
     * Test method for {@link UserService#updateUser(User)}.
     * This test case tests the scenario where user is not found in the system when trying to update it by id.
     *
     * @throws UserNotFoundException when user is not found
     */

    @Test
    void testUpdateUserUserNotFoundWithId() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setPassword("Password");
        user.setId(11111111L);

        //act
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("User " + user.getUserName() + " does not exist.", exception.getMessage());
    }

    /**
     * This test method tests the scenario where a user is trying to update their email address to an already taken email address.
     * The test creates two users with different email addresses, then tries to update the second user's email address to the first user's email address.
     * The test then asserts that an exception of type EmailHasToBeUniqueExeption is thrown with the message "The chosen E-Mail address is already taken".
     */


    @Test
    void testUpdateUserEmailTaken() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        User user2 = new User("Peter", "Musterfrau", "new@Mail.com");
        user2.setPassword("Password");
        User user2return = userService.addUser(user2);

        user2return.setMailAddress(user.getMailAddress());


        //act
        Exception exception = assertThrows(EmailHasToBeUniqueException.class, () -> userService.updateUser(user2return));

        //assert
        assertEquals("User with eMail : old@Mail.com already exists.", exception.getMessage());

    }

    /**
     * Tests the scenario where the user is not valid.
     * The test verifies that the expected UserNotValidException is thrown when a null user is passed to the isUserValid method.
     * The exception message is also verified to be "User is Null!".
     */
    @Test
    void testUserIsNotValid() {
        //arrange

        //act
        UserNotValidException userNotValidException = assertThrows(UserNotValidException.class, () -> userService.isUserValid(null));

        //assert
        assertEquals("User is Null!", userNotValidException.getMessage());

    }

    /**
     * Test method to verify if the user already exists in the repository.
     * The method first saves a default user in the repository and then tries to add the same user again.
     * An exception is expected to be thrown in this case, and the message is verified to match the expected result.
     */
    @Test
    void testUserAlreadyExists() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);

        //act
        UserAlreadyExistsException userAlreadyExistsException = assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));

        //assert
        assertEquals("User " + user.getUserName() + " exists already.", userAlreadyExistsException.getMessage());

    }


    /**
     * Test the updateUser method of the UserService class.
     * This test case tests if the email address of a user can be successfully updated.
     */

    @Test
    void testUpdateUserEmail() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setMailAddress("new@mail.address");


        //act
        userService.updateUser(user);

        //assert
        User userOnDb = userRepository.getUserById(user.getId());
        assertEquals(user.getMailAddress(), userOnDb.getMailAddress());

    }

    /**
     * Test method for {@link UserService#updateUser(User)}.
     * This test case will test the scenario where the user password is changed.
     *
     * @throws Exception when the update of user fail.
     */

    @Test
    void testUpdateUserChangePassword() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setPassword("NewPassword");


        //act
        User userOnDb = userService.updateUser(user);

        //assert
        assertEquals("NewPassword", userOnDb.getPassword());

    }

    /**
     * Test method to check if an exception is thrown when trying to update a user's password with a too short password.
     * This test method tests the validation of the password of a user, which must be at least 5 characters long and
     * consist of uppercase as well as lowercase letters.
     */
    @Test
    void testUpdateUserChangePasswordTooShort() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setPassword("shor");


        //act
        Exception exception = assertThrows(PasswordNotValidException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("Password is to short, min. 5 characters. The password must be at least 5 characters long and consist of uppercase as well as lowercase letters.", exception.getMessage());

    }

    /**
     * Test to check if the updateUser method throws a {@link PasswordNotValidException} when the new password contains none capital letters.
     * The test creates a new user and adds it to the system. Then it changes the password of the user to a password that contains none
     * capital letters and calls the updateUser method.
     * The test asserts that a {@link PasswordNotValidException} is thrown with the message "Password needs capital letters.
     * The password must be at least 5 characters long and consist of uppercase as well as lowercase letters."
     */
    @Test
    void testUpdateUserChangePasswordNoneCapitalLetters() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setPassword("short");


        //act
        Exception exception = assertThrows(PasswordNotValidException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("Password needs capital letters. The password must be at least 5 characters long and consist of uppercase as well as lowercase letters.", exception.getMessage());

    }

    /**
     * Test method to check that updating user's password with only capital letters will throw
     * {@link PasswordNotValidException} with the message "Password needs non capital letters. The password must be at least 5 characters long and consist of uppercase as well as lowercase letters."
     * This test case is for checking the validation of user's password.
     */
    @Test
    void testUpdateUserChangePasswordOnlyCapitalLetters() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setPassword("SHORT");


        //act
        Exception exception = assertThrows(PasswordNotValidException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("Password needs non capital letters. The password must be at least 5 characters long and consist of uppercase as well as lowercase letters.", exception.getMessage());

    }

    /**
     * This test method tests the behavior of the updateUser method when an invalid email is provided.
     * It creates a new user with valid email, adds it to the system, updates the email to an invalid one,
     * and then asserts that the updateUser method throws a UserNotValidException with the message
     * "E-Mail notAvalidEMail is not valid".
     */
    @Test
    void testUpdateUserEmailNotValid() {
        //arrange
        User user = new User("Max", "Mustermann", "old@Mail.com");
        user.setPassword("Password");
        userService.addUser(user);

        user.setMailAddress("notAvalidEMail");


        //act
        Exception exception = assertThrows(UserNotValidException.class, () -> userService.updateUser(user));

        //assert
        assertEquals("E-Mail notAvalidEMail is not valid", exception.getMessage());

    }

    /**
     * Test method to test the successful deletion of a user by its ID.
     * Arranges by creating and adding a new user to the repository.
     * Act by calling the deleteById method with the ID of the added user.
     * Asserts that the user is no longer present in the repository by checking for its presence with the findById method.
     */

    @Test
    void testDeleteByIdSuccess() {
        //arrange
        User user = new User("username", "firstname", "mail@example.com");
        user.setPassword("Password");
        User addedUser = userService.addUser(user);

        //act
        userService.delete(user);

        //assert
        assertFalse(userRepository.findById(addedUser.getId()).isPresent());
    }

}
