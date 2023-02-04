package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.GenerateTestuserWithToken;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.dto.UserDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.objectmapper.UserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for UserRestController.
 * This class is used to test the UserRestController class. It uses the Spring Boot Test framework
 * to test the REST endpoints for the UserRestController class. The class uses the @SpringBootTest,
 *
 * @author Niklaus Haenggi
 * @version 1.0
 * @AutoConfigureMockMvc, and @Autowired annotations to configure the test environment and
 * inject necessary dependencies.
 * The class also extends from EmptyDb class, which is used to clean up the database before each test.
 * @since 15.1.2023
 */


@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest extends GenerateTestuserWithToken {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * Test method to check that updating a user's information returns a 200 OK status code and updates the user's information in the database.
     * The method creates an example user, sends a PUT request to the "/api/users/" endpoint with a JSON body containing the updated user's information,
     * and expects a 200 OK status code in response. It also asserts that the user's information in the database has been updated.
     *
     * @throws Exception if an error occurs while performing the request
     */

    @Test
    void updateUser() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user);
        userDto.setUserName("UpdatedName");


        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto))).andExpect(status().isOk());

        User updatedUser = userRepository.getUserById(user.getId());
        assertEquals("UpdatedName", updatedUser.getUserName());
        assertEquals(user.getFirstName(), updatedUser.getFirstName());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.getMailAddress(), updatedUser.getMailAddress());

        // No Passwords to Frontend
        assertNull(userDto.getPassword());
    }


    /**
     * Test for updating the email address of a user in the system.
     * <p>
     * The test generates a user and updates its email address.
     * The test asserts that the email address of the user has been successfully updated in the system.
     * The password is not returned to the frontend.
     * </p>
     */

    @Test
    void updateUserEMailAddress() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user);
        userDto.setMailAddress("new@mail.address");


        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto))).andExpect(status().isOk());

        User updatedUser = userRepository.getUserById(user.getId());
        assertEquals(user.getUserName(), updatedUser.getUserName());
        assertEquals(user.getFirstName(), updatedUser.getFirstName());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals("new@mail.address", updatedUser.getMailAddress());

        // No Passwords to Frontend
        assertNull(userDto.getPassword());
    }

    /**
     * Test method to update the password of a user.
     *
     * @throws Exception if the update request fails
     */

    @Test
    void updateUserPassword() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user);
        userDto.setPassword("new Password");


        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto))).andExpect(status().isOk());

        User updatedUser = userRepository.getUserById(user.getId());
        assertEquals(user.getUserName(), updatedUser.getUserName());
        assertEquals(user.getFirstName(), updatedUser.getFirstName());
        assertEquals("new Password", updatedUser.getPassword());
        assertEquals(user.getMailAddress(), updatedUser.getMailAddress());

    }


    /**
     * Test method to check that deleting a user with a valid id returns a 200 OK status code and deletes the user from the database.
     * The method creates an example user, sends a DELETE request to the "/api/users/" endpoint with the user's id as a parameter,
     * and expects a 200 OK status code in response. It also asserts that the user has been deleted from the database.
     *
     * @throws Exception if an error occurs while performing the request
     */


    @Test
    public void deleteUser_validId_shouldDeleteUserAndReturnOK() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        User user2 = TestDataGenerator.GET_DEFAULT_USER();
        user2.setMailAddress("another@mailadress.ch");
        User user3 = TestDataGenerator.GET_DEFAULT_USER();
        user3.setMailAddress("another@mailadress.com");
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);


        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/").header("Authorization", "Bearer " + token).param("userId", String.valueOf(user.getId())).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertNull(userRepository.getUserById(user.getId()));
        assertTrue(userRepository.existsById(user2.getId()));
        assertTrue(userRepository.existsById(user3.getId()));
    }


    /**
     * Test case for deleteUserButUserDoesNotExist method.
     * The test case checks the scenario where the user with the given id doesn't exist and returns a NOT_FOUND status.
     *
     * @throws Exception if any error occurs during the test execution
     */
    @Test
    public void deleteUserButUSerDoesNotExist() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        User user2 = TestDataGenerator.GET_DEFAULT_USER();
        user2.setMailAddress("another@mailadress.ch");
        User user3 = TestDataGenerator.GET_DEFAULT_USER();
        user3.setMailAddress("another@mailadress.com");
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        String userIdDoesNotExist = "123456789";

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/").header("Authorization", "Bearer " + token).param("userId", userIdDoesNotExist).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();


        //assert
        assertEquals("User with Id 123456789 does not exist.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
        assertNull(userRepository.getUserById(Long.parseLong(userIdDoesNotExist)));

        assertTrue(userRepository.existsById(user.getId()));
        assertTrue(userRepository.existsById(user2.getId()));
        assertTrue(userRepository.existsById(user3.getId()));
    }

    /**
     * Tests the deletion of a user and their associated categories and cards.
     *
     * @throws Exception if the mockMVC request throws an exception
     */
    @Test
    public void deleteUser_WithCategoriesAndCards() throws Exception {

        User user = TestDataGenerator.GET_DEFAULT_USER();
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY_TO_USER(user);
        List<Card> cards = TestDataGenerator.GET_TEST_CARDS(category);
        userRepository.save(user);
        categoryRepository.save(category);
        cardRepository.saveAll(cards);


        assertNotNull(userRepository.getUserById(user.getId()));
        assertNotNull(categoryRepository.findCategoriesByOwner(user));
        assertNotNull(cardRepository.findCardByCategory(category));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/").header("Authorization", "Bearer " + token).param("userId", String.valueOf(user.getId())).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertNull(userRepository.getUserById(user.getId()));

    }


    /**
     * Test method to check that deleting a user with an invalid id returns a 404 Not Found status code and a specific error message.
     * The method sends a DELETE request to the "/api/users/" endpoint with an invalid user id as a parameter,
     * and expects a 404 Not Found status code in response. It also asserts that the error message returned is the expected one.
     *
     * @throws Exception should throw UserNotFound exception
     */


    @Test
    public void deleteUser_InValidId_ReturnNotFound() throws Exception {


        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/").header("Authorization", "Bearer " + token).param("userId", "123456789").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();

        // Assert
        assertEquals("User with Id 123456789 does not exist.", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage(), "Test failed");
    }


    /**
     * Test method to check that getting all users returns a 200 OK status code and a list of all users in the database.
     * The method creates an example user and sends a GET request to the "/api/users" endpoint. It expects a 200 OK status code in response
     * and asserts that the response body contains the information of the created user.
     *
     * @throws Exception if an error occurs while performing the request
     */


    @Test
    void getAllUsers() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        User user2 = TestDataGenerator.GET_DEFAULT_USER();
        user2.setMailAddress("another@mailadress.ch");
        User user3 = TestDataGenerator.GET_DEFAULT_USER();
        user3.setMailAddress("another@mailadress.com");
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);


        MvcResult result = mockMvc.perform(get("/api/users").header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();

        TypeReference<List<UserDto>> mapType = new TypeReference<>() {
        };
        List<User> users = objectMapper.readValue(result.getResponse().getContentAsString(), mapType).stream().map((UserDto userDto) -> userMapper.toUser(userDto)).toList();
        assertEquals(4, users.size());

        assertTrue(TestDataCompare.COMPARE_USER(user, users.get(1)));
        assertTrue(TestDataCompare.COMPARE_USER(user2, users.get(2)));
        assertTrue(TestDataCompare.COMPARE_USER(user3, users.get(3)));
    }
}
