package db.server.group;

import db.server.user.User;

import javax.persistence.*;

@Entity
public class Pairing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User gifter, receiver;

    public Pairing(User gifter, User receiver) {
        this.gifter = gifter;
        this.receiver = receiver;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
