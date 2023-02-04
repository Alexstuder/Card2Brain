package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.dto.LearnDto;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.Learn;
import ch.zhaw.card2brain.services.CardService;
import ch.zhaw.card2brain.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LearnMapper is a component class that maps a LearnDto to a Learn object and retrieves a Category object by its id.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class LearnMapper {

    /**
     * The CardService instance used to retrieve a Card object by its id.
     */
    @Autowired
    private CardService cardService;
    /**
     * The CategoryService instance used to retrieve a Category object by its id.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Maps a LearnDto to a Learn object.
     *
     * @param learnDto the LearnDto to be mapped
     * @return the mapped Learn object
     */
    public Learn toLearn(LearnDto learnDto) {
        return new Learn(learnDto.isCorrect(), cardService.getCard(learnDto.getCardId()));
    }

    /**
     * Retrieves a Category object by its id.
     *
     * @param categoryId the id of the category to be retrieved
     * @return the Category object with the specified id
     * @throws CategoryNotFound if a category with the specified id cannot be found
     */
    public Category getCardsToLearn(long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
}
