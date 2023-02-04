package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.dto.CategoryDto;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.services.CategoryService;
import ch.zhaw.card2brain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping between {@link Category} and {@link CategoryDto} objects.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class CategoryMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * This method maps a {@link Category} object to a {@link CategoryDto} object
     *
     * @param category the category object to be mapped
     * @return the mapped categoryDto object
     */

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto(category.getCategoryName(), category.getOwner().getId());
        categoryDto.setId(category.getId());

        return categoryDto;
    }

    /**
     * This method mapps CategoryDto to Category
     *
     * @param categoryDto the category object containing the name to search for
     * @return a list of categories with the given name
     */

    public Category toCategory(CategoryDto categoryDto) throws RuntimeException {

        Category category = new Category();
        User user = userService.getUserById(categoryDto.getOwner()); // throws UserNotFoundException

        if (categoryDto.id != null) {
            category = categoryService.getCategoryById(categoryDto.getId()); // throws CategoryNotExistsException
        } else {
            category.setId(null);
        }
        category.setCategoryName(categoryDto.getCategoryName());
        category.setOwner(user);

        return category;
    }


    /**
     * This method maps a user id to a {@link User} object
     *
     * @param userId id of user
     * @return the mapped User object
     */


    public User fromIdToUser(Long userId) {
        User user = new User();
        user.setId(userId);
        user = userService.getUser(user); //throws exception
        return user;
    }


    /**
     * This method maps a category id to a {@link Category} object
     *
     * @param categoryId id of category
     * @return the mapped category object
     */
    public Category fromIdToCategory(long categoryId) {
        return categoryService.getCategoryById(categoryId);// throws NotFound Exception;

    }
}
