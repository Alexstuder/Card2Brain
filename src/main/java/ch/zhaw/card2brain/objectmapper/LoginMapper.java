package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.model.User;
import org.springframework.stereotype.Component;

/**
 * The LoginMapper class provides methods for converting between User and LoginDto objects and for retrieving a User .
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class LoginMapper {

    /**
     Maps a LoginDto to a User.
     @param loginDto the LoginDto to map to a User
     @return a User instance with values from the LoginDto
     */
    public User toUser(LoginDto loginDto) {
        User user = new User();
        user.setMailAddress(loginDto.getMailAddress());
        user.setPassword(loginDto.getPassword());

        return user;
    }

}
