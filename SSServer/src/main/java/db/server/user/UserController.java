package db.server.user;

import db.server.notification.Notification;
import db.server.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

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
     * @param user User to be deleted
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    private void deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
    }

    @RequestMapping("/update")
    private User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping("/addItem/{item}")
    private List<String> addItemToWishList(@RequestBody User user, @PathVariable String item) {
        return userService.addItemToWishList(user, item);
    }

    @RequestMapping("/removeItem/{index}")
    private List<String> removeItemFromWishList(@RequestBody User user, @PathVariable int index) {
        return userService.removeItemFromWishList(user, index);
    }

    @RequestMapping("/getWishList")
    private List<String> getWishList(@RequestBody User user) {
        return userService.getWishList(user);
    }

    @RequestMapping("/getNotifications")
    private List<Notification> getNotifications(@RequestBody User user) {
        return notificationService.getNotificationsForUser(user);
    }

    @RequestMapping("/markNotification/{status}")
    private List<Notification> markNotification(@RequestBody Notification n, @PathVariable boolean status) {
        return notificationService.markNotification(n, status);
    }
}
