package ch.zhaw.card2brain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RepetitionServiceImpl is a service class that implements the RepetitionService interface.
 * It provides the functionality to calculate the next repetition date for a flashcard based on the number of correct answers in a row.
 * The class uses a pre-defined repetition sequence to determine the number of days until the next repetition.
 * The repetition sequence is as follows:
 * 0 - Always repeat
 * 1 - Repeat after 2 days
 * 2 - Repeat after 3 days
 * 3 - Repeat after 5 days
 * 4 - Repeat after 7 days
 * 5 - Repeat after 9 days
 * 6 - Repeat after 14 days
 * 7 - Repeat after 30 days
 * 8 - Repeat after 45 days
 * 9 - Repeat after 60 days
 * >9 - Repeat after 90 days
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Service
public class RepetitionServiceImpl implements RepetitionService {

    private final List<Integer> repetitionSequence = new ArrayList<>();

    @Autowired
    private Clock clock;


    public RepetitionServiceImpl() {
        fillRepetitionSequence();
    }

    private void fillRepetitionSequence() {

      /*-----------------------------------------------------------
        0   |      wird immer repetiert
        1   |      wird nach 2 Tagen wieder repetiert
        2   |      wird nach  3 Tagen wieder repetiert
        3   |      wird nach  5 Tagen wieder repetiert
        4   |      wird nach  7 Tagen wieder repetiert
        5   |      wird nach  9 Tagen wieder repetiert
        6   |      wird nach  14 Tagen wieder repetiert
        7   |      wird nach  30 Tagen wieder repetiert
        8   |      wird nach  45 Tagen wieder repetiert
        9   |      wird nach  60 Tagen wieder repetiert
        >9  |      wird nach  90 Tagen wieder repetiert
        ------------------------------------------------------------- */

        this.repetitionSequence.add(0);
        this.repetitionSequence.add(2);
        this.repetitionSequence.add(3);
        this.repetitionSequence.add(5);
        this.repetitionSequence.add(7);
        this.repetitionSequence.add(9);
        this.repetitionSequence.add(14);
        this.repetitionSequence.add(30);
        this.repetitionSequence.add(45);
        this.repetitionSequence.add(60);
        this.repetitionSequence.add(90);
    }

    public LocalDate getNextRepetitionDate(int correctAnswersInRow) {

        LocalDate nextRepetitionDate;
        if (correctAnswersInRow >= repetitionSequence.size()) {
            nextRepetitionDate = LocalDate.now(clock).plusDays(repetitionSequence.get(repetitionSequence.size() - 1));
        } else {
            nextRepetitionDate = LocalDate.now(clock).plusDays(repetitionSequence.get(correctAnswersInRow));
        }
        return nextRepetitionDate;
    }

}
