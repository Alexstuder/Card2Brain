package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.CardNotFoundException;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;

import java.util.List;

/**

 CardService is an interface that defines the methods to perform CRUD operations on Card objects.

 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */

public interface CardService {

    /**
     * Adds a Card object to the database.
     *
     * @param card The card object to be added
     * @return The Card object that was added
     */
    Card addCard(Card card);

    /**
     * Updates an existing Card object in the database.
     *
     * @param card The card object to be updated
     * @return The updated Card object
     */
    Card updateCard(Card card);

    /**
     * Deletes a Card object from the database.
     *
     * @param card The card object to be deleted
     */
    void deleteCard(Card card);

    /**
     * Deletes all Card objects of a specific Category from the database.
     *
     * @param category The category whose cards are to be deleted
     */
    void deleteCardsOfACategory(Category category);

    /**
     * Retrieves all Card objects of a specific Category from the database.
     *
     * @param category The category whose cards are to be retrieved
     * @return A List of Card objects belonging to the specified category
     */
    List<Card> getAllCardsOfACategory(Category category);

    /**
     * Retrieves all Card objects of a specific Category from the database by category ID.
     *
     * @param categoryId The ID of the category whose cards are to be retrieved
     * @return A List of Card objects belonging to the specified category
     */
    List<Card> getAllCardsOfACategoryById(long categoryId);

    /**
     * Retrieves a single Card object from the database by its ID.
     *
     * @param cardId The ID of the card to be retrieved
     * @return The Card object with the specified ID
     * @throws CardNotFoundException if the card with the specified ID cannot be found
     */
    Card getCard(Long cardId) throws CardNotFoundException;

    /**
     * Retrieves a List of Card objects for repetition based on a category ID.
     *
     * @param categoryID The ID of the category whose cards are to be retrieved for repetition
     * @return A List of Card objects for repetition
     */
    List<Card> getCardsForRepetition(long categoryID);

    /**
     * Marks a Card object as repeated, based on whether the user answered correctly.
     *
     * @param card        The Card object to be marked as repeated
     * @param rightAnswer A boolean value indicating whether the user answered correctly
     */
    void repeated(Card card, boolean rightAnswer);

    /**
     * Retrieves a List of Card objects for repetition based on a Category object.
     *
     * @param category The Category whose cards are to be retrieved for repetition
     * @return A List of Card objects for repetition
     */
    List<Card> getCardsForRepetition(Category category);

    boolean isCardValid(Card card);
}