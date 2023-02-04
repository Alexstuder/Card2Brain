package ch.zhaw.card2brain;

import ch.zhaw.card2brain.auth.AuthenticationService;
import ch.zhaw.card2brain.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
/**

 GenerateTestuserWithToken is an abstract class that extends the {@link EmptyDb} class.

 It generates a test user and token for testing purposes.
 * @see EmptyDb
 * @author Alexander Studer
 * @author Niklaus Hänggi
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
public abstract class GenerateTestuserWithToken extends EmptyDb {


    /**
     * Autowired instance of the authentication service.
     */

    protected String token;

    @Autowired
    AuthenticationService authenticationService;
    /**
     Initializes the class and generates a test user with a token.
     Invokes the {@link EmptyDb#emptyDB()} method to empty the database.
     */

    @PostConstruct
    public void afterInit() {
        super.emptyDB();

        // get User Token
        User tokenUser = new User("niklaus", "haenggi", "tokenTestUser2@Tests.com");
        tokenUser.setPassword("Password");
        token = authenticationService.register(tokenUser).getToken();
    }
}
