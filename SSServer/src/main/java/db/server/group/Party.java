package db.server.group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import db.server.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Party() {}

    public Party(User owner) {
        id = -1;
        this.owner = owner;
        members = new ArrayList<>();
        pairings = new ArrayList<>();
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public User getOwner() {
        return owner;
    }

    public void addMember(User member) {
        if (members.contains(member))
            return;
        else
            members.add(member);
    }

    public void removeMember(User member) {
        members.remove(member);
        // TODO adjust pairings
    }

    public List<Pairing> getPairings() {
        return pairings;
    }

    public void assignPairings() {
        // TODO
    }

}
