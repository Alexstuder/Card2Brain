package ch.zhaw.card2brain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The AuthenticationResponse class is used to encapsulate the authentication token returned by the authentication service.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller* @version 1.0
 * @since 28-01-2023
 */
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
 /*
 The JWT-Token for further authentication
 */
 @Getter
 private String token;
}
