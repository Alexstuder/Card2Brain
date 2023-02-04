package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.CardAlreadyExistsException;
import ch.zhaw.card2brain.exception.CardNotFoundException;
import ch.zhaw.card2brain.exception.CardNotValidException;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.repository.CardRepository;
import ch.zhaw.card2brain.util.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * This class provides the implementation for the CardService interface. It handles CRUD operations for cards and provides methods for learning cards.
 * It uses the RepetitionService, UserService and CardRepository to perform its operations.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class CardServiceImpl implements CardService, HasLogger {

    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private CardRepository cardRepository;


    /**
     * This method adds a card to the card repository.
     *
     * @param card the card to be added
     * @return the added card
     * @throws CardAlreadyExistsException if the card already exists
     */
    // CRUD operations
    @Override
    @Transactional
    public Card addCard(Card card) {
        if (card.getId() == null) { // to add a new card;id has to be null
            isCardValid(card); //throws notvalidesception
            cardRepository.save(card);
            getLogger().info("User adds a Card: User :" + card.getCategory().getOwner().getMailAddress() + " to Category :" + card.getCategory().getCategoryName() + " Card Id" + card.getId());
        } else {
            if (isCardValid(card)) {
                throw new CardAlreadyExistsException("Card exists Already");
            }

        }
        return card;
    }


    /**
     * Updates an existing Card in the database.
     *
     * @param card the Card object to update.
     * @return the updated Card object.
     * @throws CardNotFoundException if the Card with the given ID does not exist in the database.
     */
    @Override
    @Transactional
    public Card updateCard(Card card) {
        if (!cardRepository.existsById(card.getId())) {
            throw new CardNotFoundException("Card does not exist");
        }
        isCardValid(card);//throws exception
        getLogger().info("User updates  Card: User :" + card.getCategory().getOwner().getMailAddress() + " to Category :" + card.getCategory().getCategoryName() + " Card Id" + card.getId());
        return cardRepository.save(card);
    }

    /**
     * Deletes a Card from the database.
     * @param card the Card object to delete.
     */

    @Override
    @Transactional
    public void deleteCard(Card card) {
        getLogger().info("User deletes a Card: User :" + card.getCategory().getOwner().getMailAddress() + " to Category :" + card.getCategory().getCategoryName() + " Card Id" + card.getId());
        cardRepository.delete(card);

    }

    /**
     * Deletes all Cards belonging to a specific Category from the database.
     * @param category the Category object whose Cards are to be deleted.
     */

    @Override
    @Transactional
    public void deleteCardsOfACategory(Category category) {
        cardRepository.deleteAllInBatch(getAllCardsOfACategory(category));

    }

    /**
     * Retrieves all Cards belonging to a specific Category from the database.
     *
     * @param category the Category object whose Cards are to be retrieved.
     * @return a List of Card objects belonging to the given Category.
     */


    @Override
    public List<Card> getAllCardsOfACategory(Category category) {
        return cardRepository.findCardByCategory(category);
    }



    /**
     * Retrieves all Cards belonging to a specific Category from the database, based on the Category's ID.
     *
     * @param categoryId the ID of the Category whose Cards are to be retrieved.
     * @return a List of Card objects belonging to the given Category.
     */
    @Override
    public List<Card> getAllCardsOfACategoryById(long categoryId) {
        return cardRepository.findCardByCategory_Id(categoryId);
    }


    /**
     * Retrieves a Card from the database, based on its ID.
     *
     * @param cardId the ID of the Card to be retrieved.
     * @return a Card object with the given ID.
     * @throws CardNotFoundException if a Card with the given ID does not exist in the database.
     */

    @Override
    public Card getCard(Long cardId) throws CardNotFoundException {

        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Card does not exist"));
    }


    /**
     * Retrieves a list of Cards from the database that need to be repeated.
     *
     * @param categoryId the ID of the Category whose Cards are to be retrieved for repetition.
     * @return a List of Card objects that need to be repeated for the given Category.
     */

    // learn operations

    @Override
    public List<Card> getCardsForRepetition(long categoryId) {
        return cardRepository.findAllByCategoryIdAndNextDateToRepeatIsLessThanEqual(categoryId, LocalDate.now());
    }

    /**
     * Update the repetition count and answer status of a card after a repetition session.
     *
     * @param card the card to be updated
     * @param rightAnswer whether the answer was correct or not
     */
    @Override
    @Transactional
    public void repeated(Card card, boolean rightAnswer) {
        if (rightAnswer) {
            card.setCounterRight(card.getCounterRight() + 1);
            card.setCorrectAnswersInRow(card.getCorrectAnswersInRow() + 1);
        } else {
            card.setCounterFalse(card.getCounterFalse() + 1);
            card.setCorrectAnswersInRow(0);
        }
        card.setNextDateToRepeat(repetitionService.getNextRepetitionDate(card.getCorrectAnswersInRow()));
        card.setAnsweredLastTime(LocalDateTime.now());
        cardRepository.save(card);

    }

    /**
     * Retrieve all the cards that are due for repetition within a category.
     *
     * @param category the category to retrieve the cards from
     * @return a list of all the cards that are due for repetition
     */

    @Override
    public List<Card> getCardsForRepetition(Category category) {
        return cardRepository.findAllByCategoryAndNextDateToRepeatIsLessThanEqual(category, LocalDate.now());
    }


    /**
     * Method to check if a card is valid.
     *
     * @param card The card to be checked for validity.
     * @return true if the card is valid, false otherwise.
     * @throws CardNotValidException if the card is null, or if either the question or answer field is empty.
     */

    @Override
    public boolean isCardValid(Card card) {
        if (card == null) {
            throw new CardNotValidException("Card is Null!");
        }
        if (card.getAnswer().trim().equals("")) {
            throw new CardNotValidException("Card is invalid. Please enter an answer.");
        }
        if (card.getQuestion().trim().equals("")) {
            throw new CardNotValidException("Card is invalid. Please enter a question.");
        }
        return true;
    }
}
