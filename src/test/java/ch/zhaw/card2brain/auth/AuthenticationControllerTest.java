package ch.zhaw.card2brain.auth;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.config.JwtService;
import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.dto.UserDto;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.objectmapper.UserMapper;
import ch.zhaw.card2brain.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest extends EmptyDb {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserMapper userMapper;


    /**
     * Test the register a new User in the authentication controller.
     * This test is used to check if the Register endpoint is working correctly and returning a token
     *
     * @throws Exception if an error occurs during the process
     */

    @Test
    void testRegister() throws Exception {
        // prepare
        UserDto userDto = new UserDto("niklaus", "haenggi", "nik@laus.com");
        userDto.setPassword("Password");


        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto))).andExpect(status().isOk()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // check response Token
        JSONObject obj = new JSONObject(response.getContentAsString());
        String token = obj.get("token").toString();
        String userEmail = jwtService.extractUsername(token);
        assertEquals("nik@laus.com", userEmail);

    }


    /**
     * Test method for {@link #testCreateUserWithInvalidPassword()}.
     * This method tests if the user registration throws the correct exception for an invalid password.
     * It checks for three different scenarios:
     * Password is too short
     * Password needs at least one lowercase character
     * Password needs at least one uppercase character
     * It also checks that the registration is successful when the password is valid.
     */
    @Test
    void testCreateUserWithInvalidPassword() throws Exception {
        // arrange
        UserDto user = new UserDto("username", "firstname", "kiuzuikztiuzgiuztr@email.com");
        user.setPassword("shor");

        int exceptionCounter = 0;
        boolean succses = false;
        String error;
        MvcResult mvcResult;
        MockHttpServletResponse response;

        mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isNotAcceptable()).andReturn();
        response = mvcResult.getResponse();
        error = response.getContentAsString();
        succses = error.contains("Password is to short, min. 5 characters");
        assertTrue(succses);
        if (succses) exceptionCounter++;

        user.setPassword("short");
        succses = false;
        mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isNotAcceptable()).andReturn();
        response = mvcResult.getResponse();
        error = response.getContentAsString();
        succses = error.contains("Password needs capital letters");
        assertTrue(succses);
        if (succses) exceptionCounter++;
        succses = false;

        user.setPassword("SHORT");
        succses = false;
        mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isNotAcceptable()).andReturn();
        response = mvcResult.getResponse();
        error = response.getContentAsString();
        succses = error.contains("Password needs non capital letters");
        assertTrue(succses);
        if (succses) exceptionCounter++;


        user.setPassword("Correct");
        succses = false;
        mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user))).andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse();
        String token = response.getContentAsString();
        succses = !token.contains("Password needs") && !token.contains("Password is to short");
        assertTrue(succses);
        if (!succses) exceptionCounter++;


        assertEquals(3, exceptionCounter);
    }


    /**
     * Test method to check that creating a user with a duplicate email address returns a 409 Conflict status code and a specific error message.
     * The method creates an example user and then attempts to create another user with the same email address,
     * which should trigger a conflict. It also asserts that the error message returned is the expected one.
     *
     * @throws Exception should throw EmailHasToBeUnique exception
     */
    @Test
    void createUserMailIsAlreadyTaken() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        User user2 = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);


        UserDto userDto = userMapper.toUserDto(user2);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto))).andExpect(status().isConflict()).andReturn();


        String exception = mvcResult.getResolvedException().getMessage();
        assertEquals("User with eMail : " + user2.getMailAddress() + " already exists.", exception);


    }


    /**
     * Test method for {@link AuthenticationController#authenticate(LoginDto)}.
     * Test to authenticate endpoint by sending a login request with valid loginDto and checking that a valid JWT is returned.
     *
     * @throws Exception
     */


    @Test
    public void testAuthenticate() throws Exception {


        // prepare
        User tokenUser = new User("niklaus", "haenggi", "tokenTestUser@Tests.com");
        tokenUser.setPassword(passwordEncoder.encode("Password"));
        userService.addUser(tokenUser);
        LoginDto loginDto = new LoginDto(tokenUser.getMailAddress(), "Password");


        // act
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk()).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        // check response Token
        JSONObject obj = new JSONObject(response.getContentAsString());
        String token = obj.get("token").toString();
        String userEmail = jwtService.extractUsername(token);
        assertEquals("tokenTestUser@Tests.com", userEmail);

    }


}
