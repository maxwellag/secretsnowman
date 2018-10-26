package db.server.party;

import db.server.user.User;
import db.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PairingRepository pairingRepository;
    @Autowired
    private UserService userService;

    public Party makeParty(int ownerId, String partyName) {
        if (userService.userExists(ownerId)) {
            User owner = userService.getUser(ownerId);
            Party party = new Party(owner, partyName);           // initialize
            party = partyRepository.save(party);      // generate ID, properties etc
            party = addMember(party.getId(), owner);
            userService.saveUserInternal(owner);      // to register the party for it
            return party;
        } else
            return new Party();
    }

    public List<Party> getPartiesWithMemberId(int memberId) {
        User member = userService.getUser(memberId);
        if (member == null) {
            return new ArrayList<>();
        } else {
            return member.getParties();
        }
    }

    /**
     * Adds a member to the party, adds the party to the member, creates a pairing for the member
     * all if partyId and member exist.
     * @param partyId The partyID to add the member to
     * @param member The member to add to the party
     * @return The Party the member was added to (only used internally)
     */
    public Party addMember(int partyId, User member) {
        Party party = getPartyById(partyId);
        if (party != null) {
            member = userService.getUser(member.getId());
            if (!member.equals(new User())) {
                party.addMember(member);
                userService.addPartyToUserInternal(member, party);
                Pairing p = new Pairing(member, null, party);
                party.getPairings().add(p);
                pairingRepository.save(p);
                return partyRepository.save(party);
            }
        }
        return null;
    }

    public void removeMember(int groupId, User member) {
        Party party = getPartyById(groupId);
        member = userService.getUser(member.getId());
        if (party != null && member != null) {
            party.removeMember(member);
            resetPartyPairings(party);              // All pairings are now void due to incomplete matchings
            party = partyRepository.save(party);
            member.getParties().remove(party);
            userService.saveUserInternal(member);
        }
        if (party.getMembers().size() < 1) {
            throw new IllegalStateException("Party cannot have zero members!");
        }
    }

    public Party makePairings(int partyId) {
        Party party = getPartyById(partyId);
        if (party == null) {
            return new Party();
        } else {
            randomizePairings(party);
            return partyRepository.save(party);
        }
    }

    /**
     * Returns the party member on the receiving end of the pairing if any (may be null if pairings aren't made)
     * @param partyId The party to be searching through
     * @param gifter The member who is the gifter in the pairing
     * @return The party member on the receiving end of the pairing if any (may be null if pairings aren't made)
     */
    public User getGiftReceiver(int partyId, User gifter) {
        Party party = getPartyById(partyId);
        if (party == null) {
            return new User();
        } else {
            for (Pairing p : party.getPairings()) {         // Loop until you find where this gifter is the gifter
                if (p.getGifter().equals(gifter))
                    return p.getReceiver();
            }
            return new User();
        }
    }

    private Party getPartyById(int partyId) {
        try {
            return partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void randomizePairings(Party party) {
        List<User> members = party.getMembers();
        if (members.size() < 2)
            return;
        resetPartyPairings(party);                  // erase old pairings
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
        for (int i = 0; i < members.size(); i++) {          // iterate through each member, popping a member off stack
            Pairing p = party.getPairings().get(i);
            User gifter = p.getGifter();
            User receiver = receivers.pop();
            if (receiver.equals(gifter)) {                  // get the next member and replace old receiver onto stack
                if (receivers.isEmpty()) {                  // illegal state, try again
                    randomizePairings(party);
                    return;
                } else {
                    User temp = receiver;
                    receiver = receivers.pop();
                    receivers.push(temp);
                }
            }
            p.setReceiver(receiver);
            pairingRepository.save(p);
        }
    }

    private void resetPartyPairings(Party party) {
        for (Pairing p : pairingRepository.findByParty_Id(party.getId()))   // Pairings may not be fetched automatically
            pairingRepository.delete(p);
        List<Pairing> pairings = new ArrayList<>();
        for (User member : party.getMembers()) {
            Pairing p = new Pairing(member, null, party);
            pairings.add(p);
            pairingRepository.save(p);
        }

    }


    public Party getParty(int partyId) {
        try {
            return partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return new Party();
        }
    }
}
