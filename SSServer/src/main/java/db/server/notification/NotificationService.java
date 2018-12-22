package db.server.notification;

import db.server.party.Party;
import db.server.user.User;
import db.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Service
public class NotificationService {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationRepository notificationRepo;

    public void notifyUser(User user, String contents, Object o) {
        Notification not = null;
        if (o instanceof User) {        // right now, this case is a user has added an item to their wishlist only
            User giftee = (User) o;
            not = new ItemNotification(user, contents, giftee);
        }
        else if (o instanceof Party) {
            Party p = (Party) o;
            not = new PartyNotification(user, contents, p);
        }
        if (not != null)
            notificationRepo.save(not);
    }


    public List<Notification> getNotificationsForUser(User user) {
        user = userService.getUser(user.getId());
        if (user == null)
            return new ArrayList<>();
        return notificationRepo.findByOwner_Id(user.getId());
    }

    public List<Notification> markNotification(Notification n, boolean status) {
        //noinspection finally
        try {
            n = notificationRepo.findById(n.getId()).get();     // NSEE could happen here
            n.setMarkedAsRead(status);
            notificationRepo.save(n);
        } finally {
            //noinspection ReturnInsideFinallyBlock
            return getNotificationsForUser(n.getOwner());
        }
    }
}
