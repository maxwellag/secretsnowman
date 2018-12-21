package db.server.notification;

import db.server.user.User;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User owner;

    /* TODO how to efficiently store different types of data for a notification
            ie sometimes we want to go to the giftee's wishlist, but there
            are other use cases? Party has been created/you've been added
            to a party
     */

    private String contents;

    private boolean markedAsRead;

    Notification() {}

    Notification(User owner, String contents) {
        id = -1;
        this.owner = owner;
        this.contents = contents;
        markedAsRead = false;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public User getOwner() {
        return owner;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getContents() {
        return contents;
    }

    public void setMarkedAsRead(boolean markedAsRead) {
        this.markedAsRead = markedAsRead;
    }
    public boolean isMarkedAsRead() {
        return markedAsRead;
    }

}
