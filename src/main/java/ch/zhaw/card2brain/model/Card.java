package ch.zhaw.card2brain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
The class representing a database entity of a card
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
*/
@Entity(name = "Card")
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Card extends BaseEntity {

    @Getter
    @Setter
    @NonNull
    private String question;

    @Getter
    @Setter
    @NonNull
    private String answer;

    @Getter
    @Setter
    private int counterRight = 0;

    @Getter
    @Setter
    private int counterFalse = 0;

    @Getter
    @Setter
    private LocalDateTime answeredLastTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    @Getter
    @Setter
    private LocalDate nextDateToRepeat = LocalDate.of(1970, 1, 1);


    @Getter
    @Setter
    private int correctAnswersInRow = 0;

    @Getter
    @ManyToOne
    @NonNull
    @Setter
    private Category category;

}
