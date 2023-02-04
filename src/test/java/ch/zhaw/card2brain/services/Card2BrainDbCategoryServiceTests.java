package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.exception.CategoryAlreadyExistsException;
import ch.zhaw.card2brain.exception.CategoryNotFoundException;
import ch.zhaw.card2brain.exception.CategoryNotValidException;
import ch.zhaw.card2brain.exception.UserNotFoundException;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.objectmapper.CategoryMapper;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**

 Test class for {@link RepetitionServiceImpl}
 The class uses JUnit 5 and Mockito to test the functionality of {@link RepetitionServiceImpl}
 @SpringBootTest is used to load the spring context in order to test the autowired components.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
@SpringBootTest
class Card2BrainDbCategoryServiceTests extends EmptyDb {

    private static final String GESCHICHTE = "Geschichte";
    private static final String MUSIK = "Musik";
    private static final String DEUTSCH = "Deutsch";
    private static final String GEOMETRIE = "Geometrie";
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    private User testUser;
    private List<Category> testCategories;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;


    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "test", "test@mail.com");
        testUser.setPassword("Password");
        userRepository.save(testUser);
        testCategories = Arrays.asList(new Category("TestCategory1", testUser), new Category("TestCategory2", testUser), new Category("TestCategory3", testUser));


    }

    /**

     Creates a stream of arguments for testing invalid categories.
     @return stream of arguments, containing invalid categories with empty or blank names and a dummy user.
     */

    private static Stream<Arguments> readNotValidCategory() {
        return Stream.of(
                Arguments.of(new Category("", new User("any", "any", "a@a.a"))),
                Arguments.of(new Category("      ", new User("any", "any", "a@a.a")))
        );
    }

    /**
     * Test the {@link CategoryService#createCategory(Category)} method.
     * This test should check that the method correctly creates a new category and returns it,
     * also it should check that the returned category has the correct id, name and owner.
     */
    @Test
    public void testCreateCategory() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = new Category("Test category", user);

        // Act
        Category responseCategory = categoryService.createCategory(category);

        // Assert
        assertNotNull(responseCategory.getId());
        assertEquals(category.getCategoryName(), responseCategory.getCategoryName());
        assertEquals(category.getOwner(), responseCategory.getOwner());
        assertNotNull(categoryRepository.getCategoryById(responseCategory.getId()));
    }


    /**

     Test for the {@link CategoryService#createCategory(Category)} method when the category already exists in the database.
     This test checks that the exception thrown by the service method is correct.
     */
    @Test
    public void testCreateCategoryButCategoryAlreadyExists() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = new Category("Test category", user);
        categoryRepository.save(category);

        // Act
        CategoryAlreadyExistsException categoryAlreadyExistsException = assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.createCategory(category);
        });

        // Assert

        assertEquals(categoryAlreadyExistsException.getMessage(), "Creation of category failed, Category already exists!");
    }


    /**

     Test for creating a category with a pre-defined ID.
     <p>
     The test saves a user and sets the ID of the category with an invalid Id
     The act section performs the createCategory method, which should throw a CategoryNotValidException.
     The assert section checks that the exception message is as expected.
     */
    @Test
    public void testCreateCategoryButCategoryIdIsNotNull() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = new Category("Test category", user);
        category.setId(123456789L);
        categoryRepository.save(category);

        // Act
        CategoryNotValidException categoryNotValidException = assertThrows(CategoryNotValidException.class, () -> {
            categoryService.createCategory(category);
        });

        // Assert

        assertEquals(categoryNotValidException.getMessage(), "Category ID has to be null for creation");
    }

    /**

     Tests the behavior of the {@link CategoryService#createCategory(Category)} method when the category name is empty.
     @param category The category to be tested.
     */

    @ParameterizedTest
    @MethodSource("readNotValidCategory")
    public void testCreateCategoryButCategoryIsEmpty(Category category) {
        // Arrange
        userRepository.save(category.getOwner());

        // Act
        CategoryNotValidException categoryNotValidException = assertThrows(CategoryNotValidException.class, () -> {
            categoryService.createCategory(category);
        });

        // Assert
        assertEquals(categoryNotValidException.getMessage(), "Category is empty. Please enter a valid category.");
    }


    /**
     * This method retrieves all categories of a user.
     *
     * @return a list of Category objects representing the categories of the user
     * @throws CategoryNotFoundException if the user has no categories
     * @throws UserNotFoundException     if the user does not exist
     */

    @Test
    public void testGetAllCategoriesOfUser() {
        // Prepare
        categoryRepository.saveAll(testCategories);

        // When
        List<Category> result = categoryService.getAllCategoriesOfUser(categoryMapper.fromIdToUser(testUser.getId()));

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(testCategories.size());
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).getCategoryName()).isEqualTo(testCategories.get(i).getCategoryName());

        }
    }

    /**
     * Test the getAllCategoriesOfUser method when the user does not exist
     *
     * @throws UserNotFoundException when the user does not exist
     */


    @Test
    public void testGetAllCategoriesOfNotValidUser() {
        // Given
        long notExistUserId = 123456789;
        String expectedMessage = "User with ID " + notExistUserId + " does not exist.";
        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            categoryService.getAllCategoriesOfUser(categoryMapper.fromIdToUser(notExistUserId));
        });
        // Then
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }


    /**
     * Test the {@link CategoryService#updateCateory(Category)} method.
     * This test should check that the method correctly updates an existing category and returns it,
     * also it should check that the returned category has the correct id, name and owner.
     * Also it should check that the repository has the updated category.
     */

    @Test
    public void testUpdateCategory() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        category.setOwner(user);
        categoryRepository.save(category);
        category.setCategoryName("Updated category");

        // Act
        Category updatedCategory = categoryService.updateCateory(category);

        // Assert
        assertNotNull(updatedCategory);
        assertEquals(category.getId(), updatedCategory.getId());
        assertEquals(category.getCategoryName(), updatedCategory.getCategoryName());
        assertEquals(category.getOwner(), updatedCategory.getOwner());
        assertEquals(category.getCategoryName(), categoryRepository.getCategoryById(category.getId()).getCategoryName());
    }

    /**

     Test case for {@link CategoryService#updateCateory(Category)}.
     Tests that updating a category with empty name will result in a {@link CategoryNotValidException}
     with error message "Category is empty. Please enter a valid category.".
     */
    @Test
    public void testUpdateCategoryButCategoryIsEmpty() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        category.setOwner(user);
        categoryRepository.save(category);
        category.setCategoryName("");

        // Act
        CategoryNotValidException categoryNotValidException = assertThrows(CategoryNotValidException.class, () -> {
            categoryService.updateCateory(category);
        });

        // Assert
        assertEquals(categoryNotValidException.getMessage(), "Category is empty. Please enter a valid category.");
    }


    /**
     * Test the {@link CategoryService#updateCateory(Category)} method when the category id is not valid.
     * This test should check that the method throws a {@link CategoryNotFoundException} with the correct message.
     */
    @Test
    void testUpdateCategoryWithInvalidId() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = new Category("Test category", user);
        category.setId(123456789L);

        // Assert thrown
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCateory(category));
        assertEquals("Category with categoryId :" + category.getId() + " not found.", exception.getMessage());
    }

    /**

     Test to verify the deletion of a category with a valid ID.
     The following steps are performed in the test:
     <ol>
     <li>Arrange: A user and a category is saved in the database</li>
     <li>Act: The category is deleted using the delete() method of the categoryService</li>
     <li>Assert: The existence of the category in the database is verified using existsById() method of the categoryRepository</li>
     </ol>
     */
    @Test
    public void testDeleteCategoryWithValidId() {
        // Arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        userRepository.save(user);
        Category category = TestDataGenerator.GET_DEFAULT_CATEGORY();
        category.setOwner(user);
        categoryRepository.save(category);

        // Act
        categoryService.delete(category);

        // Assert
        assertFalse(categoryRepository.existsById(category.getId()));
    }


    /**
     * Test method for {@link CategoryServiceImpl#categoryExistsByUser(User, Category)}.
     * This test checks if the categoryExistsByUser method correctly detects if a category exists for a user.
     * It first creates a new category and saves it to the repository, then calls the categoryExistsByUser method
     * passing the user and category as arguments. Finally, it asserts that the method returns true, indicating that
     * the category exists for the user.
     */

    @Test
    public void testCategoryExistsByUser() {
        // Given

        Category category1 = new Category("test", testUser);
        categoryRepository.save(category1);

        // When
        boolean result = categoryService.categoryExistsByUser(testUser, category1);

        // Then
        assertThat(result).isTrue();
    }

    /**
     * Test the {@link CategoryService#getAllCategoriesOfUser(User)} method.
     * This test should check that the method correctly returns all categories of a user and the owner of each category is the same as the user id.
     */

    @Test
    void testCategoryServiceGetAllCategoriesToUser() {
        //arrange
        User user = TestDataGenerator.GET_DEFAULT_USER();
        List<Category> compareCategories = new ArrayList<>();
        compareCategories.add(TestDataGenerator.GET_TEST_CATEGORY(GEOMETRIE, user));
        compareCategories.add(TestDataGenerator.GET_TEST_CATEGORY(GESCHICHTE, user));
        compareCategories.add(TestDataGenerator.GET_TEST_CATEGORY(DEUTSCH, user));
        compareCategories.add(TestDataGenerator.GET_TEST_CATEGORY(MUSIK, user));

        userRepository.save(user);
        //categoryServiceNew.
        categoryRepository.saveAll(compareCategories);


        //act
        List<Category> categories = categoryService.getAllCategoriesOfUser(categoryMapper.fromIdToUser(user.getId()));

        //assert
        assertTrue(TestDataCompare.COMPARE_CATEGORY_LISTS(compareCategories, categories));

    }

}
