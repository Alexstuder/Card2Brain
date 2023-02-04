package ch.zhaw.card2brain.repository;

import ch.zhaw.card2brain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface for {@link UserRepository} which extends {@link JpaRepository} and provides additional methods for
 * retrieving {@link User} objects
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a {@link User} by their mail address
     *
     * @param mailAddress the mail address of the user
     * @return the {@link User} object with the specified mail address
     */
    User getUserByMailAddress(String mailAddress);

    /**
     * Retrieves a {@link User} by their id
     *
     * @param Id the id of the user
     * @return the {@link User} object with the specified id
     */
    User getUserById(long Id);

    /**
     * Retrieves all {@link User} objects
     *
     * @return a list of all {@link User} objects
     */
    List<User> findAll();

    /**
     * Retrieves a {@link User} by their username
     *
     * @param username the username of the user
     * @return the {@link User} object with the specified username
     */
    User getUserByUserName(String username);

    /**
     * Retrieves a {@link User} by their mail address
     *
     * @param mailaddress the mail address of the user
     * @return the {@link User} object with the specified mail address
     */
    Optional<User> findUserByMailAddress(String mailaddress);
}
