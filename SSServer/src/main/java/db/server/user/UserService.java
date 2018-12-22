package db.server.user;

import db.server.notification.NotificationService;
import db.server.party.Party;
import db.server.party.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private WishListItemRepository itemRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PartyService partyService;

    public Hashtable<String, User> getAllUsers() {
        Hashtable<String, User> users = new Hashtable<>();
        for (User u : userRepo.findAll()) {
            users.put(u.getUsername(), u);
        }
        return users;
    }

    User addUser(User user) {
        if (usernameAvailable(user.getUsername()))
            return userRepo.save(user);
        else
            return new User();
    }

    public User getUser(int userId) {
        try {
            User ret = userRepo.findById(userId).get();
            ret.setWishList(getWishListItems(ret));
            return ret;
        } catch (NoSuchElementException e) {
            return new User();
        }
    }

    User login(User user) {
        List<User> users = userRepo.findByUsername(user.getUsername());
        if (users == null || users.size() != 1)
            return new User();
        if (users.get(0).passwordMatch(user))
            return users.get(0);
        else
            return new User();
    }

    void deleteUser(User user) {
        userRepo.deleteById(user.getId());
        // TODO Delete all dependencies
    }

    User updateUser(User user) {
        User oldUser = getUser(user.getId());
        if (oldUser == null || oldUser.equals(new User())) {
            return new User();      // No User with that ID was found
        }
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user);
        oldUser.setParties(user.getParties());
        return userRepo.save(oldUser);
    }

    List<String> addItemToWishList(User user, String item) {
        user = getUser(user.getId());
        if (user == null || user.equals(new User())) {
            return new ArrayList<>();
        }
        WishListItem listItem = new WishListItem(user, item);
        listItem = itemRepo.save(listItem);
        user.getWishList().add(listItem);
        // TODO notify all gifters
        for (User g : partyService.getGivers(user))
            notificationService.notifyUser(g, "An item has been added to your buddy's wishlist:\n" + item, user);
        List<String> strings = new LinkedList<>();
        for (WishListItem i : user.getWishList())
            strings.add(i.getEntry());
        return strings;
    }

    List<String> removeItemFromWishList(User user, int index) {
        List<String> strings = new LinkedList<>();
        try {
            user = getUser(user.getId());
            if (user == null || user.equals(new User()))
                return new ArrayList<>();
            WishListItem item = user.getWishList().remove(index);   // could have IOOBE here
            itemRepo.delete(item);
        } finally {
            if (user != null)
                for (WishListItem i : user.getWishList())
                    strings.add(i.getEntry());
        }
        return strings;
    }

    List<String> getWishList(User user) {
        user = getUser(user.getId());
        if (user == null)
            return new ArrayList<>();
        List<String> strings = new LinkedList<>();
        for (WishListItem i : user.getWishList())
            strings.add(i.getEntry());
        return strings;
    }

    public User saveUserInternal(User user) {
        User oldUser;
        try {
            oldUser = userRepo.findById(user.getId()).get();
        } catch (NoSuchElementException e) {
            return new User();
        }
        oldUser.setParties(user.getParties());
        return userRepo.save(oldUser);
    }

    private boolean usernameAvailable(String username) {
        List<User> users = userRepo.findByUsername(username);
        return users == null || users.size() != 1;
    }

    public boolean userExists(int userId) {
        if (userId < 1)
            return false;
        else {
            try {
                userRepo.findById(userId).get();  // NSEE could happen here
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

    /**
     * Adds a member to the party if they exist and are not already in the party
     * @param member The member to add
     * @param party The party to add the member to
     * @return True if the User was successfully added, false otherwise
     */
    public boolean addPartyToUserInternal(User member, Party party) {
        member = getUser(member.getId());
        if (member != null) {
            if (!member.getParties().contains(party)) {
                member.getParties().add(party);
                userRepo.save(member);
                return true;
            }
        }
        return false;
    }

    private List<WishListItem> getWishListItems(User user) {
        List<WishListItem> items = itemRepo.findByOwner_Id(user.getId());
        if (items == null)
            return new ArrayList<>();
        return items;
    }
}
