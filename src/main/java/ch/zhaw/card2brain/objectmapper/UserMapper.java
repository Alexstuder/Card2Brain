package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.dto.UserDto;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The UserMapper class provides methods for converting between User and UserDto objects and for retrieving a User.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class UserMapper {

    @Autowired
    UserService userService;

    /**
     * Converts a UserDto object to a User object.
     *
     * @param userDto The UserDto object to be converted.
     * @return The converted User object.
     * @throws RuntimeException when the user is not found
     */
    public User toUser(UserDto userDto) throws RuntimeException {
        User user = new User(userDto.getUserName(), userDto.getFirstName(), userDto.getMailAddress());
        user.setPassword(userDto.getPassword());
        if (userDto.getId() != null) {
            userService.getUserById(userDto.getId());// throws UserNotFoundException
            user.setId(userDto.getId());
        }

        return user;
    }

    /**
     * Converts a User object to a UserDto object.
     *
     * @param user The User object to be converted.
     * @return The converted UserDto object.
     */
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(user.getUserName(), user.getFirstName(), user.getMailAddress());
        if (user.getId() != null) {
            userDto.setId(user.getId());
        }
        return userDto;
    }

    /**
     * Retrieves the User object associated with the given userId.
     *
     * @param userId The unique identifier for the User to retrieve.
     * @return The User object associated with the given userId.
     */
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }
}
