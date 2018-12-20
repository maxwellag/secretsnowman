package db.server.user;

import javax.persistence.*;

@Entity
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User owner;

    private String entry;

    WishListItem(User owner, String entry) {
        id = -1;
        this.owner = owner;
        this.entry = entry;
    }

    WishListItem() {}

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

    public void setEntry(String entry) {
        this.entry = entry;
    }
    String getEntry() {
        return entry;
    }
}
