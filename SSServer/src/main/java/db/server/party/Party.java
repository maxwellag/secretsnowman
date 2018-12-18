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

    //@Column(columnDefinition = "varchar", length = 1024)
    //private String description;

    @ManyToMany(mappedBy = "parties")
    private List<User> members;

    @OneToMany
    private List<Pairing> pairings;

    private boolean pairingsAssigned;

    Party() {}

    Party(User owner, String partyName) {
        id = -1;
        this.owner = owner;
        this.partyName = partyName;
        members = new ArrayList<>();
        pairings = new ArrayList<>();
        pairingsAssigned = false;
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
    public Pairing removeMember(User member) {
        members.remove(member);
        if (pairings == null || pairings.isEmpty())
            return null;
        for (int i = 0; i < pairings.size(); i++)
            if (pairings.get(i).getGifter().equals(member)) {
                Pairing ret = pairings.remove(i);
                return ret;
            }
        return null;
    }

    public List<Pairing> getPairings() {
        return pairings;
    }
    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }
    public void addPairing(Pairing p) {
        this.pairings.add(p);
    }

    public void setPairingsAssigned(boolean value) {
        pairingsAssigned = value;
    }
    public boolean arePairingsAssigned() {
        return pairingsAssigned;
    }

    public List<User> getMembers() {
        return members;
    }
}
