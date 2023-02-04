package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.TestData.TestDataCompare;
import ch.zhaw.card2brain.dto.CategoryDto;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.services.CategoryService;
import ch.zhaw.card2brain.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class CategoryMapperTest extends EmptyDb {


    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    private Category category;
    private CategoryDto categoryDto;
    private User user;

    @BeforeEach
    public void setUp() {

        User user1 = new User("TestUser", "Test", "test@test1.com");
        user1.setPassword("Password");
        user = userService.addUser(user1);

        Category category1 = new Category();
        category1.setCategoryName("Test Category");
        category1.setOwner(user);
        category = categoryService.createCategory(category1);

        categoryDto = new CategoryDto("Test Category", user.getId());
    }

    @Test
    public void testToDto() {

        //arrange

        //test
        CategoryDto result = categoryMapper.toDto(category);

        //assert
        assertEquals(categoryDto.getCategoryName(), result.getCategoryName());
    }

    @Test
    public void testToCategory() {

        //arrange

        //test
        Category result = categoryMapper.toCategory(categoryDto);

        //assert
        TestDataCompare.COMPARE_CATEGORY(category, result);
    }

    @Test
    public void testFromIdToUser() {

        //arrange

        //act
        User result = categoryMapper.fromIdToUser(user.getId());


        //assert
        TestDataCompare.COMPARE_USER(user, result);

    }

    @Test
    public void testFromIdToCategory() {

        //arrange

        //act
        Category result = categoryMapper.fromIdToCategory(category.getId());

        //assert
        assertTrue(TestDataCompare.COMPARE_CATEGORY(category, result));
    }
}
