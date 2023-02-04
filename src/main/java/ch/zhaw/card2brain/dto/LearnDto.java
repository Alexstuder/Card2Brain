package ch.zhaw.card2brain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**

 LearnDto is a Data Transfer Object class.
 This class is used to get the info if the answer of a card was correct from the frontend
 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */


@RequiredArgsConstructor
@AllArgsConstructor
public class LearnDto {


    @Getter
    @Setter
    private long cardId;
    @Getter
    private boolean correct;
}
