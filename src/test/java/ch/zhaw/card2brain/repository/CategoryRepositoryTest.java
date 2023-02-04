package ch.zhaw.card2brain.repository;

import ch.zhaw.card2brain.EmptyDb;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CategoryRepositoryTest extends EmptyDb {
    private User nobody;

    /**
     * Test method for {@link CategoryRepository#save(Object)}.
     * Tests that the method correctly saves a category and retrieves it by owner.
     */


    @Test
    void saveCategory() {
        nobody = new User("Nobody", "no", "e@mail.com");
        nobody.setPassword("Password");
        userRepository.save(nobody);
        Category category = new Category("Englisch", nobody);
        categoryRepository.save(category);
        assertEquals(categoryRepository.findCategoriesByOwner(nobody).get(0).getCategoryName(), "Englisch");
    }

    /**
     * Test method for {@link CategoryRepository#findCategoriesByOwner(User)}.
     * Tests if the method returns the expected category for a specific owner.
     */
    @Test
    void findCategoriesByOwner() {
        nobody = new User("Nobody", "no", "e@mail.com");
        nobody.setPassword("Password");
        userRepository.save(nobody);
        Category category = new Category("Englisch", nobody);
        categoryRepository.save(category);
        List<Category> categoriesByOwner = categoryRepository.findCategoriesByOwner(nobody);
        assertEquals(categoriesByOwner.get(0).getCategoryName(), "Englisch");
    }

    /**
     * Test class for {@link CategoryRepository#findCategoriesByCategoryName(String)}.
     * Tests that the method returns the expected category with the given name.
     */

    @Test
    void findCategoriesByCategoryName() {
        nobody = new User("Nobody", "no", "e@mail.com");
        nobody.setPassword("Password");
        userRepository.save(nobody);
        Category category = new Category("Englisch", nobody);
        categoryRepository.save(category);
        List<Category> categoriesByName = categoryRepository.findCategoriesByCategoryName("Englisch");
        assertEquals(categoriesByName.get(0).getCategoryName(), "Englisch");
        categoryRepository.deleteAll();

    }
}
