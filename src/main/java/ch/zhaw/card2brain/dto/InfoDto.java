package ch.zhaw.card2brain.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
/**
 InfoDto is a Data Transfer Object class.

 This class is used to send the userspecific information about its categories and learning status

 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */

@RequiredArgsConstructor
@Getter
public class InfoDto {

    @NonNull
    private String CategoryName;


    @NonNull
    private long CategoryId;


    @NonNull
    private int numberOfCards;


    @NonNull
    private int toLearn;


}
