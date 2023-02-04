package ch.zhaw.card2brain.config;

import ch.zhaw.card2brain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT service to handle the generation, validation and extraction of claims from JWT tokens.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16-01-2023
 */
@Service
public class JwtService {

    private static final String SECRET_KEY = "703273357638792F413F4428472B4B6250655368566D597133743677397A2443";

    /**
     * Extracts the username from the given JWT token
     *
     * @param token JWT token to extract the username from
     * @return The extracted username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the given JWT token
     *
     * @param token          JWT token to extract the claim from
     * @param claimsResolver function to extract the claim
     * @param <T>            type of the claim
     * @return The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user with no extra claims
     *
     * @param user user to generate the token for
     * @return The generated JWT token
     */
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT token for the given user with the specified extra claims
     *
     * @param extraClaims extra claims to include in the token
     * @param user        user to generate the token for
     * @return The generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder().setClaims(extraClaims).setSubject(user.getMailAddress()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).claim("ID", user.getId().toString()).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Validates the given JWT token against the given user details
     *
     * @param token       JWT token to validate
     * @param userDetails user details to validate the token against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired
     *
     * @param token JWT token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Extracts the expiration date from a given token.
     *
     * @param token the token from which to extract the expiration date
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from a given token.
     *
     * @param token the token from which to extract claims
     * @return the extracted claims
     */

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }


    /**
     * Gets the signing key used for authentication.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
