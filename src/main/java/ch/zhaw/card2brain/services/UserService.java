package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.UserNotFoundException;
import ch.zhaw.card2brain.model.User;

import java.util.List;


/**
 * Interface for UserService, providing methods for managing and manipulating User objects.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
public interface UserService {

    /**
     * Method to update a given User object.
     *
     * @param user The User object to be updated.
     * @return The updated User object.
     */
    User updateUser(User user);

    /**
     * Method to log in a User with a given User object.
     *
     * @param loginUser The User object to be logged in.
     * @return The logged in User object.
     */
    User login(User loginUser);

    /**
     * Method to retrieve a User by their ID.
     *
     * @param userId The ID of the User to be retrieved.
     * @return The User object with the given ID.
     * @throws UserNotFoundException if the User with the given ID is not found.
     */
    User getUserById(long userId) throws UserNotFoundException;

    /**
     * Method to retrieve a User by their User object.
     *
     * @param user The User object to be retrieved.
     * @return The User object with the given User object.
     * @throws UserNotFoundException if the User with the given User object is not found.
     */
    User getUser(User user) throws UserNotFoundException;

    /**
     * Method to delete a User.
     *
     * @param user The User object to be deleted.
     * @return The deleted User object.
     */
    User delete(User user);

    /**
     * Method to add a new User.
     *
     * @param user The User object to be added.
     * @return The added User object.
     * @throws RuntimeException if a problem occurs while adding the User.
     */
    User addUser(User user) throws RuntimeException;

    /**
     * Method to retrieve a list of all Users.
     *
     * @return A list of all User objects.
     */
    List<User> findAll();

    /**
     * Method to check if a given User is valid.
     *
     * @param user The User object to be checked.
     * @throws RuntimeException if the User is not valid.
     */
    void isUserValid(User user) throws RuntimeException;
}
