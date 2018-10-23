package db.server.group;

import db.server.user.User;
import db.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserService userService;

    public Party makeParty(int ownerId) {
        if (userService.userExists(ownerId)) {
            User owner = userService.getUser(ownerId);
            Party party = new Party(owner);           // initialize
            party.addMember(owner);
            party = partyRepository.save(party);      // generate ID, properties etc
            owner.getParties().add(party);
            userService.saveUserInternal(owner);      // to register the party for it
            return party;
        } else
            return new Party(null);
    }

    public List<Party> getPartiesWithMemberId(int memberId) {
        User member = userService.getUser(memberId);
        if (member == null) {
            return new ArrayList<>();
        } else {
            return member.getParties();
        }
    }

    public void addMember(int groupId, User member) {
        Party party = getPartyById(groupId);
        if (party == null)
            return;
        else {
            party.addMember(member);
            partyRepository.save(party);
        }
    }

    public void removeMember(int groupId, User member) {
        Party party = getPartyById(groupId);
        if (party == null)
            return;
        else {
            party.removeMember(member);
            partyRepository.save(party);
        }
    }

    public Party makePairings(int partyId) {
        Party party = getPartyById(partyId);
        if (party == null) {
            return new Party(null);
        } else {
            party.assignPairings();
            return party;
        }
    }

    public User getGiftReceiver(int partyId, int memberId) {
        Party party = getPartyById(partyId);
        if (party == null) {
            return new User();
        } else {
            for (Pairing p : party.getPairings()) {         // Loop until you find where this member is the gifter
                if (p.getGifter().getId() == memberId)
                    return p.getReceiver();
            }
            return new User();
        }
    }

    private Party getPartyById(int partyId) {
        Party party;
        try {
            party = partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        return party;
    }

    public List<Party> getParties(User user) {
        User savedUser = userService.getUser(user.getId());
        if (savedUser == null)
            return new ArrayList<>();
        else
            return savedUser.getParties();
    }
}
