package ch.zhaw.card2brain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security Configuration class
 * This class is used to configure the security for the application. It enables web security,
 * and uses the {@link JwtAuthenticationFilter} and {@link AuthenticationProvider} beans to handle
 * JWT authentication and authorization.
 * It also configures the application to ignore certain routes for security checks.
 * It also enables CORS for the application
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * This bean is used to configure the web security settings.
     * In this case, it is configured to ignore all requests for h2 database console
     *
     * @return an instance of {@link WebSecurityCustomizer}
     */

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }


    /**
     * This class configures the security filter chain for the application. It sets up the {@link HttpSecurity} object to
     * disable CSRF protection, authorize requests to certain paths, and set the session creation policy to stateless.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests().requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/index.html", "/swagger-ui/**", "/v3/**", "/healthCheck/**").permitAll().anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).cors();

        return http.build();
    }
}
