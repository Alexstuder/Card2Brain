package ch.zhaw.card2brain.auth;

import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.dto.UserDto;
import ch.zhaw.card2brain.objectmapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The AuthenticationController class is a Spring REST controller for handling authentication requests such as registering and authenticating users.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller* @version 1.0
 * @since 28-01-2023
 */


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /*

    The service property is used to handle the registration and authentication of users.
    */
    @Autowired
    AuthenticationService service;
    /**
     * The userMapper property is used to map between UserDto and User objects.
     */
    @Autowired
    UserMapper userMapper;


    /**
     * The register method is used to handle user registration requests.
     *
     * @param userDto the DTO containing the user information to be registered
     * @return a response entity containing the JWT-Token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(service.register(userMapper.toUser(userDto)));
    }

    /**
     * The authenticate method is used for the user login in the frontend
     *
     * @param loginDto the DTO containing username(E-Mail) and password
     * @return a response entity containing the JWT-Token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginDto loginDto
    ) {
        return ResponseEntity.ok(service.authenticate(loginDto));
    }
}
