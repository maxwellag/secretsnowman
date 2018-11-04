package db.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Adds a user from the specified body, if one doesn't already exist
     * @param user The user to create (id field uninitialized
     * @return The user fully realized with id field - null User
     *          (uninitialized) if username exists
     */
    @RequestMapping("/add")
    private User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Attempts to login this User with the details specified in the body
     * @param user The User to be logging in
     * @return The complete User on successful login, null User otherwise
     */
    @RequestMapping("/login")
    private User login(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * Attempts to delete a User with the specified ID
     * @param userId
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    private void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping("/update")
    private User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

}
