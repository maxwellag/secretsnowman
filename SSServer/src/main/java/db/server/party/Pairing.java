package db.server.party;

import db.server.user.User;

import javax.persistence.*;

@Entity
public class Pairing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Party party;

    @ManyToOne
    private User gifter, receiver;

    public Pairing() {}

    Pairing(User gifter, User receiver, Party p) {
        this.gifter = gifter;
        this.receiver = receiver;
        this.party = p;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Party getParty() {
        return party;
    }
    public void setParty(Party party) {
        this.party = party;
    }

    public User getGifter() {
        return gifter;
    }
    public void setGifter(User gifter) {
        this.gifter = gifter;
    }

    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
