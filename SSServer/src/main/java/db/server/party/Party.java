package db.server.party;

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

    private String partyName;

    @Column(columnDefinition = "varchar", length = 1024)
    private String description;

    @ManyToMany(mappedBy = "parties")
    private List<User> members;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Pairing> pairings;

    Party() {}

    Party(User owner, String partyName) {
        id = -1;
        this.owner = owner;
        this.partyName = partyName;
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
        if (!members.contains(member)) {
            members.add(member);
        }
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
