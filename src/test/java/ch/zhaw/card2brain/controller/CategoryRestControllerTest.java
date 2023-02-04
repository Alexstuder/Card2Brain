package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.GenerateTestuserWithToken;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.auth.AuthenticationService;
import ch.zhaw.card2brain.dto.CategoryDto;
import ch.zhaw.card2brain.exception.CategoryNotFoundException;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.objectmapper.CategoryMapper;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.services.CategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class is used to test the functionality of the CategoryRestController.
 * It uses the SpringBootTest and AutoConfigureMockMvc annotations for setting up the tests and the MockMvc for performing requests.
 * The test methods use assertions to check if the response from the controller has the expected status and properties.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */

@SpringBootTest
@AutoConfigureMockMvc
class CategoryRestControllerTest extends GenerateTestuserWithToken {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Category cat1;
    private Category cat2;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    AuthenticationService authenticationService;

    private Long dbUserId;


    /**
     * setUp method is used to set up the test environment before each test case. It creates a new user in the database and assign tu categories, but not save them in the db.
     */


    @BeforeEach
    void setUp() {

        User user = new User("max", "maximilian", "e@mail.com");
        user.setPassword("Password");
        userRepository.save(user);
        User dbUser = userRepository.getUserByMailAddress("e@mail.com");
        dbUserId = dbUser.getId();
        cat1 = new Category("Informatik", user);
        cat2 = new Category("Franzoesisch", user);


    }


    /**
     * testGetAllCategoriesOfUser is a test method to check if the get request on the endpoint /api/categories/ returns a status ok
     * when the id parameter is passed in the request.
     * This test also checks if the categories are correctly saved to the repository
     *
     * @throws Exception
     */


    @Test
    public void testGetAllCategoriesOfUser() throws Exception {
        //arrange
        categoryRepository.save(cat1);
        categoryRepository.save(cat2);
        List<Category> actualList = new ArrayList<>();
        actualList.add(cat1);
        actualList.add(cat2);
        //act
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/").header("Authorization", "Bearer " + token).param("userId", cat1.getOwner().getId().toString()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        //assert return
        TypeReference<List<CategoryDto>> mapType = new TypeReference<List<CategoryDto>>() {
        };
        List<CategoryDto> responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), mapType);
        assertEquals(2, responseDto.size());

        List<Category> responsDbo = responseDto.stream().map((CategoryDto categoryDto) -> categoryMapper.toCategory(categoryDto)).toList();
        //assert DB
        List<Category> categoriesFromDb = categoryRepository.findCategoriesByOwner(cat1.getOwner());
        assertEquals(2, categoriesFromDb.size());
        assertTrue(TestDataCompare.COMPARE_CATEGORY_LISTS(actualList, responsDbo));

    }

    /**
     * Test case for the method that retrieves all categories of a not valid user
     * Verifies that when a user with a non-existent ID attempts to retrieve categories, a 404 Not Found status
     * is returned and the correct error message is displayed
     *
     * @throws Exception
     */

    @Test
    public void testGetAllCategoriesOfNotValidUser() throws Exception {
        categoryRepository.save(cat1);
        categoryRepository.save(cat2);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/").header("Authorization", "Bearer " + token).param("userId", "123456789")).andExpect(status().isNotFound()).andReturn();

        // Assert
        assertEquals("User with ID 123456789 does not exist.", mvcResult.getResolvedException().getMessage(), "Test failed");
    }

    @Test
    public void testGetAllCategoriesValidUserButHasNoCategories() throws Exception {
        User user = TestDataGenerator.GET_DEFAULT_USER();
        user.setId(12345678L);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/").header("Authorization", "Bearer " + token).param("userId", "123456789")).andExpect(status().isNotFound()).andReturn();

        // Assert
        assertEquals("User with ID 123456789 does not exist.", mvcResult.getResolvedException().getMessage(), "Test failed");
    }


    /**
     * Test case for the method that creates a new category
     * Verifies that a new category can be created and a 201 Created status is returned
     *
     * @throws Exception
     */
    @Test
    public void testCreateCategory() throws Exception {
        //arrange
        CategoryDto categoryDto = new CategoryDto("TestCategory", dbUserId);

        //act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(categoryDto))).andExpect(status().isCreated()).andReturn();

        CategoryDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        //assert
        List<Category> categoryFromDb = categoryService.getAllCategoriesOfUser(categoryMapper.fromIdToUser(dbUserId));

        assertEquals(1, categoryFromDb.size());
        assertTrue(TestDataCompare.COMPARE_CATEGORY_DTO(responseDto, categoryMapper.toDto(categoryFromDb.get(0))));
    }


    /**
     * Test case for the method that creates a new category with a user that does not exist
     * Verifies that when a user with a non-existent ID attempts to create a category, a 404 Not Found status
     * is returned and the correct error message is displayed
     *
     * @throws Exception
     */
    @Test
    public void testCreateCategoryUserNotExists() throws Exception {

        // prepare
        CategoryDto categoryDto = new CategoryDto("TestCategory", 123456789);

        // perform
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(categoryDto))).andExpect(status().isNotFound()).andReturn();

        // Assert
        assertEquals("User with Id 123456789 does not exist.", mvcResult.getResolvedException().getMessage(), "Test failed");
    }

    /**
     * Test case for the method that updates a category
     * Verifies that a category can be updated and a 200 OK status is returned
     *
     * @throws Exception
     */

    @Test
    public void testUpdateCategory() throws Exception {
        //arrange
        categoryRepository.save(cat1);
        CategoryDto categoryDto = new CategoryDto("TestCategory", dbUserId);
        categoryDto.setId(cat1.getId());


        //act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(categoryDto))).andExpect(status().isOk()).andReturn();

        //assert
        CategoryDto responseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertTrue(TestDataCompare.COMPARE_CATEGORY_DTO(categoryDto, responseDto));

    }


    /**
     * Test case for the method that deletes a category
     * Verifies that a category can be deleted and a 200 OK status is returned
     *
     * @throws Exception
     */
    @Test
    public void deleteCategoryTest() throws Exception {

        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY_TO_USER(user);
        userRepository.save(user);
        categoryRepository.save(category);

        //act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/").header("Authorization", "Bearer " + token).param("categoryId", String.valueOf(category.getId())).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        //assert
        assertNull(categoryRepository.getCategoryById(category.getId()));
    }

    @Test
    public void deleteCategoryButCategoryDoesNotExistTest() throws Exception {
        //arrange


        //act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/").header("Authorization", "Bearer " + token).param("categoryId", "123456789").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn();

        //assert
        assertEquals("Category with categoryId :123456789 not found.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void deleteCategoryButCardsExistsTest() throws Exception {

        //arrange
        categoryRepository.save(cat1);
        cardRepository.saveAll(TestDataGenerator.GET_TEST_CARDS(cat1));
        //act
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/").header("Authorization", "Bearer " + token).param("categoryId", String.valueOf(cat1.getId())).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        //assert
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.getCategoryById(cat1.getId());
        });
        assertThat(exception.getMessage()).isEqualTo("Category with categoryId :" + cat1.getId() + " not found.");
    }
}






