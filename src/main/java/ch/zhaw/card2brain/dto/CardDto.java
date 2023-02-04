package ch.zhaw.card2brain.dto;

import lombok.*;

/**

 CardDto is a Data Transfer Object class representing a Card.

 This class is used to transfer Card data between frontend and backend
 It is used to simplify the transfer of data between the Card interface and the application's business logic.
 This class is also used to validate Card input.

 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */

@RequiredArgsConstructor
@NoArgsConstructor
public class CardDto extends BaseDto {

    @Getter
    @Setter
    @NonNull
    private String question;

    @Getter
    @Setter
    @NonNull
    private String answer;

    @Getter
    @NonNull
    @Setter
    private long categoryId;


}
