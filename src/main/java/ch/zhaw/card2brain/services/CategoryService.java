package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.CategoryNotFoundException;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;

import java.util.List;

/**
 * Interface for the CategoryService class, providing methods for handling and manipulating categories.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
public interface CategoryService {

    /**
     * Retrieves a category by its ID, throwing a CategoryNotFoundException if the category cannot be found.
     *
     * @param id The ID of the category to retrieve
     * @return The category with the specified ID
     * @throws CategoryNotFoundException if the category cannot be found

     */
    Category getCategoryById(long id) throws CategoryNotFoundException;

    /**
     * Creates a new category.
     *
     * @param category The category to be created
     * @return The created category
     */
    Category createCategory(Category category);

    /**
     * Retrieves all categories associated with a specific user.
     *
     * @param user The user to retrieve categories for
     * @return A list of categories associated with the specified user
     */
    List<Category> getAllCategoriesOfUser(User user);

    /**
     * Checks if a category exists for a specific user.
     *
     * @param user     The user to check for the category
     * @param category The category to check for
     * @return True if the category exists for the user, false otherwise
     */
    boolean categoryExistsByUser(User user, Category category);

    /**
     * Validates a category.
     *
     * @param category The category to validate
     * @return True if the category is valid, false otherwise
     */
    boolean isCategoryValid(Category category);

    /**
     * Deletes a category.
     *
     * @param category The category to delete
     */
    void delete(Category category);

    /**
     * Updates a category.
     *
     * @param category The category to update
     * @return The updated category
     */
    Category updateCateory(Category category);




}
