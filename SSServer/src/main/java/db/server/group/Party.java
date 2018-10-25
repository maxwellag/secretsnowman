package db.server.group;

import db.server.user.User;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.*;

@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User owner;

    @ManyToMany(mappedBy = "parties", fetch = FetchType.EAGER)
    private List<User> members;

    @OneToMany
    private List<Pairing> pairings;

    Party() {}

    Party(User owner) {
        id = -1;
        this.owner = owner;
        members = new ArrayList<>();
        pairings = new ArrayList<>();
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

    public void addMember(User member) {
        if (!members.contains(member))
            members.add(member);
    }

    public void removeMember(User member) {
        members.remove(member);
        // TODO adjust pairings
    }

    public List<Pairing> getPairings() {
        return pairings;
    }
    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }


    public List<User> getMembers() {
        return members;
    }
}
