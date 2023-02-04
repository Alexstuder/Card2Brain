package ch.zhaw.card2brain.config;


import ch.zhaw.card2brain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * ApplicationConfig class provides beans for authentication and user management.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16-01-2023
 */

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepository userRepository;

  /**

   Returns a bean of UserDetailsService, which is used to load user-specific data in the application.
   @return UserDetailsService the UserDetailsService bean
   */

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository.findUserByMailAddress(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  /**

   The authenticationProvider method returns a bean of DaoAuthenticationProvider, which is used to authenticate the user.
   The method sets the UserDetailsService and PasswordEncoder to the DaoAuthenticationProvider instance.
   The UserDetailsService is used to retrieve user information from the repository, and the PasswordEncoder is used to encode the password before comparing it with the stored one.
   The authentication provider is returned by this method, allowing other parts of the application to use it to authenticate users.
   @return AuthenticationProvider a bean of DaoAuthenticationProvider, used for user authentication.
   */

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }


  /**

   The authenticationManager method returns a bean of AuthenticationManager, which is used for authenticating the user.
   @param config The instance of AuthenticationConfiguration required for getting the AuthenticationManager.
   @return An instance of AuthenticationManager which can be used by other parts of the application for authentication purposes.
   @throws Exception If there is any error in getting the AuthenticationManager from the given AuthenticationConfiguration.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }


  /**
   * The passwordEncoder method returns a bean of BCryptPasswordEncoder, which is used to encode the user's password before storing it in the database.
   * @return BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
