package db.server.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import db.server.party.Party;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    public User() {
        id = -1;
        parties = new ArrayList<>();
        wishList = new LinkedList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;

    private String password;

    @OneToMany
    private List<WishListItem> wishList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User_Party",
            joinColumns = @JoinColumn(name = "User_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "Party_id", referencedColumnName = "id"))
    @JsonBackReference
    private List<Party> parties;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    void setPassword(User other) {
        this.password = other.password;
    }

    public List<Party> getParties() {
        return parties;
    }
    void setParties(List<Party> parties) {
        this.parties = parties;
    }

    boolean passwordMatch(User other) {
        if (password == null)
            return false;
        return password.equals(other.password);
    }

    void setWishList(List<WishListItem> list) {
        this.wishList = list;
    }
    List<WishListItem> getWishList() {
        return wishList;
    }

    /**
     * Returns true if the two Users have the same id, disregards other fields
     * @param o The other User to be compared
     * @return True if the two Users have the same id
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return this.getId() == u.getId();
        } else
            return false;
    }

    public int hashCode() {
        return id;
    }
}
