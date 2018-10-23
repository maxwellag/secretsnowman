package db.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Hashtable<String, User> getAllUsers() {
        Hashtable<String, User> users = new Hashtable<>();
        for (User u : userRepository.findAll()) {
            users.put(u.getUsername(), u);
        }
        return users;
    }

    public User addUser(User user) {
        if (usernameAvailable(user.getUsername()))
            return userRepository.save(user);
        else
            return new User();
    }

    public User getUser(int userId) {
        try {
            return userRepository.findById(userId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public User login(User user) {
        List<User> users = userRepository.findByUsername(user.getUsername());
        if (users == null || users.size() != 1)
            return new User();
        if (users.get(0).getPassword().equals(user.getPassword()))
            return users.get(0);
        else
            return new User();
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
        // TODO Delete all dependencies
    }

    public User updateUser(User user) {
        User oldUser;
        try {
            oldUser = userRepository.findById(user.getId()).get();
        } catch (NoSuchElementException e) {
            return new User();      // No User with that ID was found
        }
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setParties(user.getParties());
        return userRepository.save(oldUser);
    }

    public User saveUserInternal(User user) {
        User oldUser;
        try {
            oldUser = userRepository.findById(user.getId()).get();
        } catch (NoSuchElementException e) {
            return new User();
        }
        oldUser.setParties(user.getParties());
        return userRepository.save(oldUser);
    }

    private boolean usernameAvailable(String username) {
        List<User> users = userRepository.findByUsername(username);
        return users == null || users.size() != 1;
    }

    public boolean userExists(int userId) {
        if (userId < 1)
            return false;
        else {
            try {
                return userRepository.findById(userId).get() != null;
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }
}
