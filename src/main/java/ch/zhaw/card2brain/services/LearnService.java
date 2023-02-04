package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.Learn;

import java.util.List;

/**
 * LearnService interface provides methods to manage and retrieve information related to learning.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
public interface LearnService {

    /**
     * This method is used to indicate that a card has been shown to the user.
     *
     * @param learn - the Learn object containing information about the card shown
     */
    void cardWasShown(Learn learn);

    /**
     * This method retrieves a list of cards to be learned for a specific category.
     *
     * @param category - the category for which to retrieve the cards
     * @return a list of Card objects to be learned for the given category
     */
    List<Card> cardsToLearn(Category category);
}
