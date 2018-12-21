package db.server.notification;

import db.server.party.Party;
import db.server.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "2")
public class PartyNotification extends Notification {

    @ManyToOne
    private Party party;

    PartyNotification() {
        super();
        party = null;
    }

    PartyNotification(User owner, String contents, Party party) {
        super(owner, contents);
        this.party = party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
    public Party getParty() {
        return party;
    }
}


