package db.server.group;

import db.server.user.User;
import db.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.*;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PairingRepository pairingRepository;
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
        if (party != null) {
            member = userService.getUser(member.getId());
            if (!member.equals(new User())) {
                party.addMember(member);
                userService.addPartyToUserInternal(member, party);
                partyRepository.save(party);
            }
        }
    }

    public void removeMember(int groupId, User member) {
        Party party = getPartyById(groupId);
        if (party != null) {
            party.removeMember(member);
            partyRepository.save(party);
        }
    }

    public Party makePairings(int partyId) {
        Party party = getPartyById(partyId);
        if (party == null) {
            return new Party(null);
        } else {
            randomizePairings(party);
            return partyRepository.save(party);
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

    private void randomizePairings(Party party) {
        List<User> members = party.getMembers();
        if (members.size() < 2)
            return;
        resetPartyPairings(party);       // erase old pairings
        Random rand = new Random();
        Stack<User> receivers = new Stack<>();      // for convenience of keeping track of what's left and quick remove
        HashSet<Integer> used = new HashSet<>();    // used in initial setup of stack
        for (int i = 0; i < members.size(); i++) {
            int selection = Math.abs(rand.nextInt()) % members.size();      // get random index to start at
            while (used.contains(selection)) {                              // if this index has been used already
                selection = (selection + 1) % members.size();
            }
            used.add(selection);
            receivers.push(members.get(selection));
        }
        for (User gifter : members) {          // iterate through each member, popping a member off stack
            User receiver = receivers.pop();
            if (receiver.equals(gifter)) {          // get the next member and replace old receiver onto stack
                if (receivers.isEmpty()) {                  // illegal state, try again
                    randomizePairings(party);
                    return;
                } else {
                    User temp = receiver;
                    receiver = receivers.pop();
                    receivers.push(temp);
                }
            }
            Pairing p = new Pairing(gifter, receiver, party);
            pairingRepository.save(p);
            party.getPairings().add(p);
        }
    }

    private void resetPartyPairings(Party party) {
        for (Pairing p : pairingRepository.findByParty_Id(party.getId()))   // Pairings may not be fetched automatically
            pairingRepository.delete(p);
        party.setPairings(new ArrayList<>());
    }


    public Party getParty(int partyId) {
        try {
            return partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return new Party();
        }
    }
}
