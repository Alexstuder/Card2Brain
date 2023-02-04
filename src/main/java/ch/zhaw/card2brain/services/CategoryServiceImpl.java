package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.CategoryAlreadyExistsException;
import ch.zhaw.card2brain.exception.CategoryNotFoundException;
import ch.zhaw.card2brain.exception.CategoryNotValidException;
import ch.zhaw.card2brain.exception.UserNotFoundException;
import ch.zhaw.card2brain.model.BaseEntity;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.util.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;


/**
 * This class provides the implementation for the CategoryService interface. It handles CRUD operations for categories and provides methods for retrieving categories of a user.
 * It uses the CategoryRepository, UserRepository, CardRepository and UserService to perform its operations. *
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class CategoryServiceImpl implements CategoryService, HasLogger {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardService cardService;

    /**
     * This method returns a list of all the categories of a given user.
     *
     * @param user The user for which the categories need to be retrieved.
     * @return A list of all the categories of the given user.
     * @throws RuntimeException If the user has any invalid categories.
     */
    @Override
    public List<Category> getAllCategoriesOfUser(User user) {

        List<Category> categories = categoryRepository.findCategoriesByOwner(user);

        // check , if all categories are valid : throws Exception
        try (Stream<Boolean> ignored = categories.stream().map(this::isCategoryValid)) {
            getLogger().debug("User gets all his categories");
        } catch (CategoryNotValidException categoryIsNotValid) {
            getLogger().debug("User has invalid categories ! User :" + user.getMailAddress());
            throw new RuntimeException(categoryIsNotValid);
        }

        return categories;
    }


    /**
     * This method checks whether a category already exists for a user.
     *
     * @param user     the user to check
     * @param category the category to check
     * @return true if the category already exists for the user, false otherwise
     */

    @Override
    public boolean categoryExistsByUser(User user, Category category) {
        List<Category> exists = categoryRepository.findCategoriesByOwner(user);
        List<Long> ids = exists.stream().map(BaseEntity::getId).toList();
        return ids.contains(category.getId());
    }

    /**
     * This method creates a new category with the given name.
     *
     * @param category the category to be created
     * @return the created category in the form of a Category object
     * @throws CategoryAlreadyExistsException if a category with the same name already exists
     * @throws UserNotFoundException          if the user associated with the category does not exist
     */

    @Override
    @Transactional
    public Category createCategory(Category category) {

        if (category.getId() != null) {
            if (categoryRepository.getCategoryById(category.getId()) != null) {
                throw new CategoryAlreadyExistsException("Creation of category failed, Category already exists!");
            } else {
                throw new CategoryNotValidException("Category ID has to be null for creation");
            }
        }

        isCategoryValid(category);//throws exception
        getLogger().info("User adds a new Category: User :" + category.getOwner().getMailAddress() + " Category :" + category.getCategoryName());
        categoryRepository.save(category);
        return category;
    }

    /**
     * This method retrieves a category by its id.
     *
     * @param categoryId the id of the category
     * @return the category in the form of a Category object
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Override
    public Category getCategoryById(long categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.getCategoryById(categoryId);

        if (category == null) {
            throw new CategoryNotFoundException("Category with categoryId :" + categoryId + " not found.");
        }
        return category;
    }

    /**
     * This method checks if a category is valid.
     *
     * @param category the category to be checked
     * @return true if the category is valid, false otherwise
     * @throws CategoryNotValidException if the category is not valid
     */
    @Override
    public boolean isCategoryValid(Category category) {
        if (category == null || category.getCategoryName().trim().equals("")) {
            throw new CategoryNotValidException("Category is empty. Please enter a valid category.");
        }
        return true;
    }

    /**
     * This method updates a category
     *
     * @param category the Category object representing the updated category
     * @return the updated category in the form of a Category object
     */
    @Override
    @Transactional
    public Category updateCateory(Category category) {
        getCategoryById(category.getId());// throws exception
        getLogger().info("User updates a Category: User :" + category.getOwner().getMailAddress() + " from Category :" + category.getCategoryName());
        category.setCategoryName(category.getCategoryName());
        isCategoryValid(category);//throws exception
        getLogger().info("User updates a Category: User :" + category.getOwner().getMailAddress() + " to Category :" + category.getCategoryName());
        categoryRepository.save(category);

        return category;
    }

    /**
     * This method retrieves a list of categories with a given name.
     *
     * @param category the category object containing the name to search for
     */
    @Override
    @Transactional
    public void delete(Category category) {
        cardService.deleteCardsOfACategory(category);
        getLogger().info("User deletes a Category: User :" + category.getOwner().getMailAddress() + " to Category :" + category.getCategoryName());
        categoryRepository.delete(category);

    }


}
