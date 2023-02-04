package ch.zhaw.card2brain.services;

import ch.zhaw.card2brain.exception.*;
import ch.zhaw.card2brain.model.User;
import ch.zhaw.card2brain.repository.UserRepository;
import ch.zhaw.card2brain.util.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The UserServiceImpl class is an implementation of the UserService interface that provides
 * a set of methods for managing user data in the application, such as adding, updating, and deleting users.
 * This class is also responsible for validating user data before storing it in the database.
 * It uses the UserRepository class to interact with the database and perform CRUD operations.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16-01-2023
 */

@Service
public class UserServiceImpl implements UserService, HasLogger {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;


    /**
     * Adds a new user to the database.
     *
     * @param user A DTO representing the user to be added.
     * @return The added user as a DTO.
     * @throws UserAlreadyExistsException  if the user already exists in the database.
     * @throws EmailHasToBeUniqueException if the email address is already taken
     * @throws UserNotValidException       if the user is not valid
     * @throws PasswordNotValidException   if the password is not valid
     */

    @Override
    @Transactional
    public User addUser(User user) throws RuntimeException {

        if (user.getId() == null) { // new userId has to be null to add a new user

            return userRepository.save(user);

        } else {
            throw new UserAlreadyExistsException("User " + user.getUserName() + " exists already.");
        }
    }


    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users as DTOs.
     */

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    /**
     * updates an existing user in the database.
     *
     * @param user A DTO representing the user to be updated.
     * @return The updated user as a DTO.
     * @throws UserNotFoundException       if the user does not exist in the database.
     * @throws EmailHasToBeUniqueException if the email address is already taken
     * @throws UserNotValidException       if the user is not valid
     * @throws PasswordNotValidException   if the password is not valid
     */

    @Override
    @Transactional
    public User updateUser(User user) throws RuntimeException {
        User userInDB = this.getUser(user);//throws exception
        if (!user.getMailAddress().contentEquals(userInDB.getMailAddress())) {
            isMailValid(user.getMailAddress());//throws exception
            isMailAddressAlreadyTaken(user.getMailAddress());//throws exception
            userInDB.setMailAddress(user.getMailAddress());
        }
        if (user.getPassword() != null) {
            isPasswordValid(user.getPassword());//throws exception
            userInDB.setPassword(user.getPassword());
        }
        userInDB.setUserName(user.getUserName());
        userInDB.setFirstName(user.getFirstName());
        userRepository.save(userInDB);
        getLogger().info("User updated from: Mail :" + user.getMailAddress() + " Username :" + user.getUserName() + " Firstname :" + user.getFirstName());
        getLogger().info("User updated  to : Mail :" + userInDB.getMailAddress() + " Username :" + userInDB.getUserName() + " Firstname :" + userInDB.getFirstName());
        return userInDB;
    }

    /**
     * This method is used to log in a user. It takes in a {@link User} object as a parameter and checks if the email address and password match those in the database.
     * If the email address and password match, the user is logged in and their information is logged in the system.
     * If the password does not match, a {@link PasswordNotValidException} is thrown.
     *
     * @param loginUser - A {@link User} object representing the user attempting to log in.
     * @return - The {@link User} object representing the logged in user.
     */
    @Override
    public User login(User loginUser) {
        User user = userRepository.getUserByMailAddress(loginUser.getMailAddress());
        isMailValid(user.getMailAddress());
        if (user.getPassword().equals(loginUser.getPassword())) {
            getLogger().info("User logged in : Mail :" + loginUser.getMailAddress() + " Username :" + loginUser.getUserName() + " Firstname :" + loginUser.getFirstName());
            return user;
        } else {
            throw new PasswordNotValidException("Password is not correct");
        }
    }

    /**
     * Deletes a given user from the database.
     * Also deletes all categories associated with the user before deleting the user itself.
     * Logs the deleted user's details (email, username, and first name).
     * Returns the deleted user's details.
     *
     * @param user the user to be deleted
     * @return the deleted user's details
     */
    @Override
    @Transactional
    public User delete(User user) {
        User userOnDb = this.getUser(user);//throws exception
        categoryService.getAllCategoriesOfUser(user).forEach(category -> categoryService.delete(category));
        getLogger().info("User deleted : Mail :" + user.getMailAddress() + " Username :" + user.getUserName() + " Firstname :" + user.getFirstName());
        userRepository.delete(user);

        return userOnDb;
    }

    /**
     * This method checks if a user with the provided userId exists in the userRepository.
     *
     * @param user the user that is to be checked for existence
     * @returns a user if a user with the provided userId exists in the userRepository.
     */

    @Override
    public User getUser(User user) throws UserNotFoundException {
        User userExists;

        if (user.getId() == null) {
            throw new UserNotFoundException(getCustomErrorMsg(user));
        }

        try {
            userExists = getUserById(user.getId());
        } catch (UserNotFoundException e) {
            // just replace the error msg
            throw new UserNotFoundException(getCustomErrorMsg(user));
        }

        return userExists;
    }

    /**
     * This method generates a custom error message for a non-existent user.
     * If the user's username is null, the message will include the user's ID.
     * If the user's ID is null, the message will include the user's username.
     *
     * @param user The user object for which the error message is being generated.
     * @return A string containing the custom error message.
     */
    private String getCustomErrorMsg(User user) {
        String nameOrID = "";
        if (user.getUserName() == null) {
            if (user.getId() != null) {
                nameOrID = "with ID " + user.getId();
            }
        } else {
            nameOrID = user.getUserName();
        }
        return "User " + nameOrID + " does not exist.";
    }

    @Override
    public User getUserById(long userId) throws UserNotFoundException {
        User userExists = userRepository.getUserById(userId);
        if (userExists == null) {
            getLogger().info("UserId does not exist :" + userId);
            throw new UserNotFoundException("User with Id " + userId + " does not exist.");
        }
        getLogger().info("User does not exist :" + userExists.getMailAddress() + " Username :" + userExists.getUserName() + " Firstname :" + userExists.getFirstName());
        return userExists;
    }

    /**
     * This method checks whether the given mail address is already taken by another user.
     *
     * @param mail The mail address to check.
     */

    public void isMailAddressAlreadyTaken(String mail) throws RuntimeException {
        if (userRepository.getUserByMailAddress(mail) != null) {
            throw new EmailHasToBeUniqueException("User with eMail : " + mail + " already exists.");
        }
    }

    /**
     * Checks if the given email is already taken by another user in the database.
     *
     * @param mailAddress the email address to check
     * @throws UserNotValidException if E-mail is invalid
     */

    private void isMailValid(String mailAddress) {
        if (mailAddress == null) {
            throw new UserNotValidException("No e-mail address was entered");
        }
        if (!mailAddress.contains("@") || !mailAddress.contains(".")) {
            throw new UserNotValidException("E-Mail " + mailAddress + " is not valid");
        }
    }

    /**
     * Checks if the given password is valid.
     *
     * @param password the password to check
     * @throws PasswordNotValidException if the password is not valid
     */
    private void isPasswordValid(String password) {
        String errorMsg = "The password must be at least 5 characters long and consist of uppercase as well as lowercase letters.";

        if (password == null) {
            throw new PasswordNotValidException("Please enter a Password." + errorMsg);
        }
        if (password.length() < 5) {
            throw new PasswordNotValidException("Password is to short, min. 5 characters. " + errorMsg);
        }
        if (password.equals(password.toLowerCase())) {
            throw new PasswordNotValidException("Password needs capital letters. " + errorMsg);
        }
        if (password.equals(password.toUpperCase())) {
            throw new PasswordNotValidException("Password needs non capital letters. " + errorMsg);
        }
    }


    /**
     * Checks if the given user object is valid.
     *
     * @param user the user object to check
     * @throws UserNotValidException if the user object is not valid
     */

    public void isUserValid(User user) throws RuntimeException {

        if (user == null) {
            throw new UserNotValidException("User is Null!");
        }
        isMailValid(user.getMailAddress());//throws exception
        isMailAddressAlreadyTaken(user.getMailAddress());//throws exception
        isPasswordValid(user.getPassword());//throws exception


    }

}
