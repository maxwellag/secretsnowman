package db.server.party;

import db.server.user.User;
import db.server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Service
class PartyService {

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PairingRepository pairingRepository;
    @Autowired
    private UserService userService;

    Party makeParty(int ownerId, String partyName, String description) {
        if (userService.userExists(ownerId)) {
            User owner = userService.getUser(ownerId);
            if (partyNameIsNotUnique(owner, partyName))
                return new Party();
            Party party = new Party(owner, partyName, description);           // initialize
            party = partyRepository.save(party);      // generate ID, properties etc
            party = addMember(party.getId(), owner);
            userService.saveUserInternal(owner);      // to register the party for it
            return party;
        } else
            return new Party();
    }

    List<Party> getPartiesWithMemberId(int memberId) {
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
    Party addMember(int partyId, User member) {
        Party party = getPartyById(partyId);
        if (party != null) {
            member = userService.getUser(member.getId());
            if (!member.equals(new User())) {
                if (!party.addMember(member))
                    return null;            // member is in the party already
                userService.addPartyToUserInternal(member, party);
                Pairing p = new Pairing(member, null, party);
                party.addPairing(p);
                pairingRepository.save(p);
                resetPartyPairings(party);
                return partyRepository.save(party);
            }
        }
        return null;
    }

    void removeMember(int groupId, User member) {
        Party party = getPartyById(groupId);
        member = userService.getUser(member.getId());
        if (party != null && member != null) {
            Pairing p = party.removeMember(member);
            pairingRepository.delete(p);
            resetPartyPairings(party);              // All pairings are now void due to incomplete matchings
            party = partyRepository.save(party);
            member.getParties().remove(party);
            userService.saveUserInternal(member);
        }
        if ((party != null ? party.getMembers().size() : 0) < 1) {
            throw new IllegalStateException("Party cannot have zero members!");
        }
    }

    Party makePairings(int partyId) {
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
    User getGiftReceiver(int partyId, User gifter) {
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

    Party getParty(int partyId) {
        try {
            return partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return new Party();
        }
    }

    List<Party> getPartiesWithOwnerId(int ownerId) {
        List<Party> parties = partyRepository.findByOwner_Id(ownerId);
        return parties == null ? new ArrayList<>() : parties;
    }

    private Party getPartyById(int partyId) {
        try {
            return partyRepository.findById(partyId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void randomizePairings(Party party) {
        List<Pairing> pairings = party.getPairings();
        if (pairings.size() < 2)
            return;
        resetPartyPairings(party);                  // erase old pairings
        Random rand = new Random();
        boolean[] indexUsed = new boolean[pairings.size()];
        for (int i = 0; i < pairings.size(); i++) {
            Pairing p = party.getPairings().get(i);
            // technique to get positive integers only
            int randIndex = rand.nextInt(pairings.size());
            if (i < pairings.size() - 1) {      // guaranteed to have a free, non-self index
                while (randIndex == i || indexUsed[randIndex])
                    randIndex = ++randIndex % pairings.size();
            } else {
                if (!indexUsed[i]) {    // This means that all other members are in a loop (must force insertion)
                    while (randIndex == i) {
                        randIndex = rand.nextInt(pairings.size());
                    }
                    User receiver = pairings.get(randIndex).getReceiver();
                    pairings.get(randIndex).setReceiver(p.getGifter());
                    p.setReceiver(receiver);
                    pairingRepository.save(pairings.get(randIndex));
                    pairingRepository.save(p);
                    party.setPairingsAssigned(true);
                    partyRepository.save(party);
                    return;
                } else {
                    while (randIndex == i || indexUsed[randIndex])  // find unused index
                        randIndex = ++randIndex % pairings.size();
                }
            }
            // Since members and pairings are not stored symmetrically (index 0 in members is not index 0 in pairings)
            // Have to use the pairings to ensure someone does not have themselves
            indexUsed[randIndex] = true;
            p.setReceiver(pairings.get(randIndex).getGifter());
            pairingRepository.save(p);
        }
        party.setPairingsAssigned(true);
        partyRepository.save(party);
    }

    /**
     * Removes all pairings from the database and initializes the list with
     * pairings with null receivers
     * @param party The Party to reset
     */
    private void resetPartyPairings(Party party) {
        for (Pairing p : party.getPairings()) {
            p.setReceiver(null);
            pairingRepository.save(p);
        }
        party.setPairingsAssigned(false);
        partyRepository.save(party);
    }

    private boolean partyNameIsNotUnique(User owner, String partyName) {
        List<Party> parties = partyRepository.findByOwner_Id(owner.getId());
        for (Party p : parties)
            if (p.getPartyName().equals(partyName))
                return true;
        return false;
    }
}
