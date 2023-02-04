package ch.zhaw.card2brain.repository;

import ch.zhaw.card2brain.model.Card;
import ch.zhaw.card2brain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface extends JpaRepository and provides methods for finding and retrieving Card objects from the database.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @see JpaRepository
 * @see Card
 * @since 16.01.2023
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**

     This method takes a long cardId as a parameter and returns a Card object that matches the provided cardId.
     @param cardId the id of the card to be searched for
     @return the Card object that matches the provided cardId
     */
    Card findCardById(long cardId);

    /**

     This method takes a Category object as a parameter and returns a List of Card objects that match the provided category.
     @param category the category of the cards to be searched for
     @return a List of Card objects that match the provided category
     */
    List<Card> findCardByCategory(Category category);

    /**

     This method takes a long categoryId as a parameter and returns a List of Card objects that match the provided categoryId.
     @param categoryId the id of the category of the cards to be searched for
     @return a List of Card objects that match the provided categoryId
     */
    List<Card> findCardByCategory_Id(long categoryId);

    /**

     This method takes a Category object and LocalDate object as parameters and returns a List of Card objects that match the provided category and have a nextDateToRepeat less than or equal to the provided LocalDate.
     @param category the category of the cards to be searched for
     @param nextDateToRepeat the date to compare against the nextDateToRepeat field of the cards
     @return a List of Card objects that match the provided category and have a nextDateToRepeat less than or equal to the provided LocalDate
     */
    List<Card> findAllByCategoryAndNextDateToRepeatIsLessThanEqual(Category category, LocalDate nextDateToRepeat);

    /**

     This method takes a long categoryId and LocalDate object as parameters and returns a List of Card objects that match the provided categoryId and have a nextDateToRepeat less than or equal to the provided LocalDate.
     @param categoryId the id of the category of the cards to be searched for
     @param nextDateToRepeat the date to compare against the nextDateToRepeat field of the cards
     @return a List of Card objects that match the provided categoryId and have a nextDateToRepeat less than or equal to the provided LocalDate
     */
    List<Card> findAllByCategoryIdAndNextDateToRepeatIsLessThanEqual(long categoryId, LocalDate nextDateToRepeat);

}
