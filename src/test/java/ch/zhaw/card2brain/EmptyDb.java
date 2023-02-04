package ch.zhaw.card2brain;

import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.repository.CategoryRepository;
import ch.zhaw.card2brain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**

 Abstract class EmptyDb provides methods to delete data from the database.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
public abstract class EmptyDb {


    /**
     * Repository for accessing {@link ch.zhaw.card2brain.repository.CardRepository} data from database.
     */
    @Autowired
    public CardRepository cardRepository;

    /**
     * Repository for accessing {@link ch.zhaw.card2brain.model.Category} data from database.
     */
    @Autowired
    public CategoryRepository categoryRepository;

    /**
     * Repository for accessing {@link ch.zhaw.card2brain.model.User} data from database.
     */


    @Autowired
    public UserRepository userRepository;

    /**
     * Method to delete all data from cardRepository, categoryRepository, and userRepository.
     * This will happen before all Tests to have an empty database
     */
    @PostConstruct
    public void emptyDB() {

        cardRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

}
