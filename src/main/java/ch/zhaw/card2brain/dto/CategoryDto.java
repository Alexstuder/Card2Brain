package ch.zhaw.card2brain.dto;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
/**

 CategoryDto is a Data Transfer Object class representing a Category.

 This class is used to transfer Category data between frontend and backend
 It is used to simplify the transfer of data between the Category interface and the application's business logic.
 This class is also used to validate Category input.

 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */
@AllArgsConstructor
@Getter
public class CategoryDto extends BaseDto {

    @Getter
    private String categoryName;

    @Getter
    @ManyToOne
    @NonNull
    private long owner;
}
