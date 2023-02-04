package ch.zhaw.card2brain.repository;

import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface for {@link CategoryRepository} which extends {@link JpaRepository} and provides additional methods for
 * retrieving {@link Category} objects
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    /**
     * This method finds all categories that belong to a specific user.
     *
     * @param owner the user whose categories will be retrieved
     * @return a list of Category objects belonging to the user
     */
    List<Category> findCategoriesByOwner(User owner);

    /**
     * This method finds all categories that have a specific name.
     *
     * @param CategoryName the name of the categories to be retrieved
     * @return a list of Category objects with the specified name
     */
    List<Category> findCategoriesByCategoryName(String CategoryName);

    /**
     * This method finds a specific category by its ID.
     *
     * @param id the ID of the category to be retrieved
     * @return the Category object with the specified ID
     */
    Category getCategoryById(Long id);
}

