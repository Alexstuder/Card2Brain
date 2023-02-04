package ch.zhaw.card2brain.auth;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.config.JwtService;
import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.exception.EmailHasToBeUniqueException;
import ch.zhaw.card2brain.exception.PasswordNotValidException;
import ch.zhaw.card2brain.exception.UserNotFoundException;
import ch.zhaw.card2brain.exception.UserNotValidException;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AuthenticationServiceTest extends EmptyDb {


    @Autowired
    AuthenticationService authenticationService;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    private User testUser;
    private LoginDto loginDto;

    @BeforeEach
    public void setUp() {
        testUser = new User("testUser", "testFirstName", "test@email.com");
        testUser.setPassword("testPassword");
        loginDto = new LoginDto("test@email.com", "testPassword");
    }

    /**
     * Test the register method of the authentication service.
     * This method tests that the register method of the authentication service correctly creates a new user and returns a token.
     */

    @Test
    public void testRegister() {
        AuthenticationResponse response = authenticationService.register(testUser);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(testUser.getMailAddress(), jwtService.extractUsername(response.getToken()));
    }

    /**
     * Test for the {@link AuthenticationService#authenticate(LoginDto)} method.
     * This test case tests the successful authentication of a user and checks the returned token.
     * It also asserts that the token is not null and the email of the authenticated user is equal to the email extracted from the token.
     */

    @Test
    public void testAuthenticate() {
        authenticationService.register(testUser);
        AuthenticationResponse response = authenticationService.authenticate(loginDto);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(testUser.getMailAddress(), jwtService.extractUsername(response.getToken()));
    }

    /**
     * Test to check if an exception is thrown when attempting to authenticate a user who doesn't exist.
     * The expected outcome is that a {@link UserNotFoundException} is thrown, with the message
     * "User with E-Mail [mailAddress] does not exsists." where [mailAddress] is the mail address of the non-existing user.
     */

    @Test
    public void testAuthenticateUserNotExist() {

        AuthenticationResponse response = null;
        try {
            response = authenticationService.authenticate(loginDto);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            System.out.println(e);
            assertEquals(UserNotFoundException.class, e.getClass());
            assertEquals("User with E-Mail " + loginDto.getMailAddress() + " does not exsists.", e.getMessage());
        }
    }


    /**
     * This test method tests the scenario where the user enters a wrong password while trying to authenticate.
     * It calls the authenticate method of the AuthenticationService class and checks if the exception thrown is of PasswordNotValidException type and if the exception message is as expected.
     */

    @Test
    public void testAuthenticateWrongPassword() {
        authenticationService.register(testUser);
        AuthenticationResponse response = null;
        LoginDto wrongPassword = new LoginDto(testUser.getMailAddress(), "WrongPassword");
        try {
            response = authenticationService.authenticate(wrongPassword);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals(PasswordNotValidException.class, e.getClass());
            assertEquals("Wrong Password, please try again", e.getMessage());
        }
    }


    /**
     * Test method for {@link AuthenticationService#register(User)} with invalid passwords
     * This test case checks if the password validation is working properly and throws the correct exception messages
     * when the password is too short, does not contain a capital letter or a non-capital letter
     *
     * @throws PasswordNotValidException if the password validation fails
     */

    @Test
    void testCreateUserWithInvalidPassword() {
        // arrange
        User user = new User("username", "firstname", "kiuzuikztiuzgiuztr@email.com");
        user.setPassword("shor");

        int exceptionCounter = 0;
        try {
            authenticationService.register((user));

        } catch (Exception e) {
            assertEquals(PasswordNotValidException.class, e.getClass());
            assertTrue(e.getMessage().contains("Password is to short, min. 5 characters"));
            exceptionCounter++;
        }
        user.setPassword("short");
        try {
            authenticationService.register((user));

        } catch (Exception f) {
            assertEquals(PasswordNotValidException.class, f.getClass());
            assertTrue(f.getMessage().contains("Password needs capital letters"));
            exceptionCounter++;
        }
        user.setPassword("SHORT");
        try {
            authenticationService.register((user));

        } catch (Exception c) {
            assertEquals(PasswordNotValidException.class, c.getClass());
            assertTrue(c.getMessage().contains("Password needs non capital letters"));
            exceptionCounter++;
        }
        user.setPassword("Correct");
        try {
            authenticationService.register((user));

        } catch (Exception d) {
            exceptionCounter++;
        }
        // act and assert

        assertEquals(3, exceptionCounter);
    }


    /**
     * This test method tests the scenario where an email address is already taken.
     * It creates a user, adds it to the database, and then attempts to add the same user again,
     * expecting an {@link EmailHasToBeUniqueException} to be thrown.
     */

    @Test
    void testRegisterUserEmailAlready() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        authenticationService.register(user);
        User user2 = TestDataGenerator.GET_DEFAULT_USER();

        //act 2 time adding same user
        Exception exception = assertThrows(EmailHasToBeUniqueException.class, () -> authenticationService.register(user2));

        //assert
        assertEquals("User with eMail : " + user2.getMailAddress() + " already exists.", exception.getMessage());
    }


    /**
     * This test method is used to test the scenario of creating a new user with an invalid email address.
     * The test creates a new {@link User} object with an invalid email address, and then attempts to add
     * the user to the system using the {@link AuthenticationService#register(User)} method.
     * The test then asserts that the method throws a {@link UserNotValidException} with the message "E-Mail is not valid".
     * This test is important to ensure that the system is able to handle and validate email addresses correctly.
     */


    @Test
    void testCreateUserWithInvalidEmail() {
        //arrange
        User user = new User("username", "firstname", "invalidemail");
        user.setPassword("Password");

        //act and assert
        assertThrows(UserNotValidException.class, () -> authenticationService.register(user), "E-Mail is not valid");
    }


}
