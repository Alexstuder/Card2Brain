package ch.zhaw.card2brain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**

 UserDto is a Data Transfer Object class representing a user.

 This class is used to transfer user data between frontend and backend
 It is used to simplify the transfer of data between the user interface and the application's business logic.
 This class is also used to validate user input.

 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */


@RequiredArgsConstructor
@Getter
@Setter
public class UserDto extends BaseDto {

    /**

     *
     * @param userName the username of the user
     */

    private String userName;

    /**

     * @param firstName the first name of the user

     */

    private String firstName;

    /**

     * @param mailAddress the email address of the user
     */

    private String mailAddress;

    @JsonProperty(required = false)
    private String password;


    public UserDto(String userName, String firstName, String mailAddress) {
        this.userName = userName;
        this.firstName = firstName;
        this.mailAddress = mailAddress;
    }
}
