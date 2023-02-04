package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.CategoryNotFoundException;
import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import ch.zhaw.card2brain.model.Learn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is the implementation of the {@link LearnService} interface.
 * It provides methods to update card's counter, next repetition date and last time card was answered,
 * as well as methods to get all cards to learn for a particular category.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 *
 * @version 1.0
 * @since 16.01.2023
 */

@Component
public class LearnServiceImpl implements LearnService {

    @Autowired
    private CardService cardService;

    @Autowired
    private RepetitionService repetitionService;

    /**
     * cardWasShown method is used to update the card's counter, next repetition date and last time card was answered.
     *
     * @param learn LearnDto object containing card id and boolean for whether the answer was correct
     */
    @Override
    public void cardWasShown(Learn learn) {
        if (learn.isCorrect()) {
            learn.getCard().setCounterRight(learn.getCard().getCounterRight() + 1);
            learn.getCard().setCorrectAnswersInRow(learn.getCard().getCorrectAnswersInRow() + 1);
        } else {
            learn.getCard().setCounterFalse(learn.getCard().getCounterFalse() + 1);
        }

        learn.getCard().setNextDateToRepeat(repetitionService.getNextRepetitionDate(learn.getCard().getCorrectAnswersInRow()));
        learn.getCard().setAnsweredLastTime(LocalDateTime.now());
        cardService.updateCard(learn.getCard());
    }

    /**
     * cardsToLearn method is used to get all cards to learn for a particular category.
     *
     * @param category
     * @return list of cardDtos
     * @throws CategoryNotFoundException if category is not found
     */
    public List<Card> cardsToLearn(Category category) {
        return cardService.getCardsForRepetition(category);

    }
}
