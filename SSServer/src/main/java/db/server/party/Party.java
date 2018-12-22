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

    private String description;

    @ManyToMany(mappedBy = "parties")
    private List<User> members;

    @OneToMany
    private List<Pairing> pairings;

    private boolean pairingsAssigned;

    Party() {}

    Party(User owner, String partyName, String description) {
        id = -1;
        this.owner = owner;
        this.partyName = partyName;
        this.description = description;
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

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
    String getPartyName() {
        return partyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }


    /**
     * Adds member to this party if they are not already in it
     * @param member Member to be added
     * @return True if the member was added successfully, false otherwise
     */
    boolean addMember(User member) {
        if (!members.contains(member)) {
            members.add(member);
            return true;
        }
        return false;
    }
    Pairing removeMember(User member) {
        members.remove(member);
        if (pairings == null || pairings.isEmpty())
            return null;
        for (int i = 0; i < pairings.size(); i++)
            if (pairings.get(i).getGifter().equals(member)) {
                return pairings.remove(i);
            }
        return null;
    }
    public void setMembers(List<User> members) {
        this.members = members;
    }

    List<Pairing> getPairings() {
        return pairings;
    }
    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }
    void addPairing(Pairing p) {
        this.pairings.add(p);
    }

    void setPairingsAssigned(boolean value) {
        pairingsAssigned = value;
    }
    boolean arePairingsAssigned() {
        return pairingsAssigned;
    }

    List<User> getMembers() {
        return members;
    }
}
