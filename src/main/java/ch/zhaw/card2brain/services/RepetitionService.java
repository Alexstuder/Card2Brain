package ch.zhaw.card2brain.services;

import java.time.LocalDate;

/**
 * RepetitionService interface provides a method to calculate the next repetition date based on the number of correct answers in a row.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
public interface RepetitionService {


    /**
     * Gets the next repetition date.
     *
     * @param correctAnswersInRow the number of correct answers in a row
     * @return the next repetition date
     */
    LocalDate getNextRepetitionDate(int correctAnswersInRow);
}
