package db.server.notification;

import db.server.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "1")
public class ItemNotification extends Notification {

    @ManyToOne
    private User giftee;

    ItemNotification() {
        super();
        giftee = null;
    }

    public ItemNotification(User owner, String contents, User giftee) {
        super(owner, contents);
        this.giftee = giftee;
    }

    public void setGiftee(User giftee) {
        this.giftee = giftee;
    }
    public User getGiftee() {
        return giftee;
    }
}
