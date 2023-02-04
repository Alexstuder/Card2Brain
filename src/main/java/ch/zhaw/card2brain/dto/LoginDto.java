package ch.zhaw.card2brain.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
/**
 LoginDto is a Data Transfer Object class.
 This class is used to get username and password from frontend by the login process

 @author Niklaus Hänggi
 @author Alexander Studer
 @author Roman Joller
 @version 1.0
 @since 16.01.2023
 */

@RequiredArgsConstructor
@Getter
public class LoginDto {

    @NonNull
    private String mailAddress;



    @NonNull
    private String password;


}
