package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.dto.CardDto;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.services.CardService;
import ch.zhaw.card2brain.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CardMapper class is responsible for mapping between Card and CardDto objects.
 * It uses two services, CategoryService and CardService, to perform necessary database operations.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */

@Component
public class CardMapper {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CardService cardService;


    /**
     * Maps a Card object to a CardDto object.
     *
     * @param card The Card object to be mapped.
     * @return The mapped CardDto object.
     */
    public CardDto mapToCardDto(Card card) {
        CardDto cardDto = new CardDto(card.getQuestion(), card.getAnswer(), card.getCategory().getId());
        cardDto.setId(card.getId());// cuz a cardDto.Id is null ; when adding a card
        return cardDto;
    }

    /**
     * Maps a CardDto object to a Card object.
     *
     * @param cardDto The CardDto object to be mapped.
     * @return The mapped Card object.
     * @throws NotFoundException if the category specified in the cardDto does not exist.
     */
    public Card mapToCard(CardDto cardDto) {
        Category category = categoryService.getCategoryById(cardDto.getCategoryId()); // throws exception

        Card card = new Card();
        card.setCategory(category);
        card.setId(cardDto.getId());
        card.setAnswer(cardDto.getAnswer());
        card.setQuestion(cardDto.getQuestion());

        return card;
    }

    /**
     * Maps an ID to a Card object.
     *
     * @param cardId The ID of the Card object to be mapped.
     * @return The mapped Card object.
     * @throws NotFoundException if the card or the category specified in the card does not exist.
     */
    public Card mapFromIdToCard(long cardId) {

        Card card = cardService.getCard(cardId); // throws NotFound Exception
        categoryService.getCategoryById(card.getCategory().getId()); //throws NotFound exception

        return card;
    }

}
