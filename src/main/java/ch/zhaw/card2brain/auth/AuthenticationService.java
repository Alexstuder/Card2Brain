package ch.zhaw.card2brain.auth;


import ch.zhaw.card2brain.config.JwtService;
import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.exception.PasswordNotValidException;
import ch.zhaw.card2brain.exception.UserNotFoundException;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The AuthenticationService class is a service that handles user registration and authentication.
 * It uses a {@link UserRepository} to access user data, a {@link PasswordEncoder} to encrypt passwords,
 * a {@link JwtService} to generate JSON web tokens, and an {@link AuthenticationManager} for authentication.
 * It also uses a {@link UserService} to validate user data.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller* @version 1.0
 * @since 28-01-2023
 */

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;


    /**
     * This method is used to register a new user.
     * It validates the user data using the {@link UserService#isUserValid(User)} method,
     * encrypts the password using the {@link PasswordEncoder},
     * adds the user to the repository using the {@link UserService#addUser(User)},
     * and generates a JSON web token using the {@link JwtService#generateToken(User)} method.
     *
     * @param user The user to register
     * @return An {@link AuthenticationResponse} containing the generated JSON web token
     * @throws RuntimeException if the user data is invalid
     */

    public AuthenticationResponse register(User user) throws RuntimeException {


        userService.isUserValid(user);

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userService.addUser(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    /**
     * This method is used to authenticate a user.
     * It uses the {@link AuthenticationManager#authenticate(Authentication)}
     * method to authenticate the user, then retrieves the user from the repository using the
     * {@link UserRepository#findUserByMailAddress(String)} method.
     * It then generates a JSON web token using the {@link JwtService#generateToken(User)} method.
     *
     * @param loginDto A DTO containing the user's email address and password
     * @return An {@link AuthenticationResponse} containing the generated JSON web token
     * @throws RuntimeException if the user is not found in the repository
     */

    public AuthenticationResponse authenticate(LoginDto loginDto) throws RuntimeException {

        var user = repository.findUserByMailAddress(loginDto.getMailAddress())
                .orElseThrow(() -> new UserNotFoundException("User with E-Mail " + loginDto.getMailAddress() + " does not exsists."));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getMailAddress(),
                            loginDto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new PasswordNotValidException("Wrong Password, please try again");
        }


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



}
