package ch.zhaw.card2brain.controller;


import ch.zhaw.card2brain.dto.InfoDto;
import ch.zhaw.card2brain.dto.UserDto;
import ch.zhaw.card2brain.objectmapper.UserMapper;
import ch.zhaw.card2brain.services.InfoService;
import ch.zhaw.card2brain.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * RestController class for User entity.
 * It uses the Spring RestController
 * annotation to indicate that it is a REST controller. It also uses the RequestMapping annotation to map
 * the class to the "/api/users" endpoint.
 * Each method uses the appropriate HTTP method annotation, such as @PostMapping,
 *
 * @author Alexander Studer
 * @author Niklaus Haenggi
 * @author Roman Joller
 * @version 1.0
 * PutMapping, DeleteMapping, and GetMapping to handle the corresponding HTTP requests.
 * The method also returns the appropriate HTTP status code and response body.
 * @since 15.1.2023
 */

@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {


    private final UserServiceImpl userService;

    private final InfoService infoService;

    private final UserMapper userMapper;

    /**
     * The constructor for the UserRestController class. It uses the @Autowired annotation to inject the necessary dependencies
     *
     * @param userService an implementation of the UserService interface to handle user-related logic
     * @param infoService a service class to handle user's information related logic
     * @param userMapper to convert UserDto to User and back
     */

    @Autowired
    public UserRestController(UserServiceImpl userService, InfoService infoService, UserMapper userMapper) {
        this.userService = userService;
        this.infoService = infoService;
        this.userMapper = userMapper;
    }

    /**
     * Endpoint for updating a user's information.
     * This endpoint is used to update a user's information by providing their new information in a UserDto object.
     * The method takes in a UserDto object as a request body, which contains the updated information of the user.
     * If the update is successful, it returns a ResponseEntity with the UserDto object as the body and a 200 OK status code.
     *
     * @param userDto a UserDto object containing the updated information of the user
     * @return a ResponseEntity with the UserDto object as the body and a 200 OK status code if user update is successful
     */

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto returnDto = userMapper.toUserDto(userService.updateUser(userMapper.toUser(userDto)));
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the user to delete
     * @return a {@link ResponseEntity} with an HTTP status of OK if the user was successfully deleted
     */

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping(value = "/")
    public ResponseEntity<UserDto> deleteUser(@RequestParam long userId) {
        userService.delete(userMapper.getUserById(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves a list of all users and returns them in the form of a
     * {@link ResponseEntity} with an {@link HttpStatus#OK} status.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserDto} objects and an {@link HttpStatus#OK} status.
     */


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }


    /**
     * Retrieves a list of information for a user by their ID.
     *
     * @param userId the ID of the user
     * @return a {@link ResponseEntity} with a list of {@link InfoDto} objects and an HTTP status of OK if the infos were successfully retrieved
     */

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/info")
    public ResponseEntity<List<InfoDto>> getInfosOfUser(@RequestParam long userId) {
        List<InfoDto> infos = infoService.getInfos(userMapper.getUserById(userId));
        return new ResponseEntity<>(infos, HttpStatus.OK);
    }


}
