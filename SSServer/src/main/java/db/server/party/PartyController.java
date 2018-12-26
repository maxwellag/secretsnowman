package db.server.party;

import db.server.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;

    /**
     * This method makes a party with the string description as the first object in the
     * String[] body. (It is an array because I don't know how bare Strings work as
     * JSON objects)
     * @param description The description for the party
     * @param ownerId The Id of the owner who is making the party
     * @param partyName The name of the party being made (must be unique to that user)
     * @return The constructed Party with the specified fields (id = -1 if error)
     */
    @RequestMapping("/make/{ownerId}/{partyName}")
    public Party makeParty(@RequestBody String[] description, @PathVariable int ownerId,
                           @PathVariable String partyName) {
        return partyService.makeParty(ownerId, partyName, description[0]);
    }

    @RequestMapping("/get/{partyId}")
    public Party getParty(@PathVariable int partyId) {
        return partyService.getPartyById(partyId);
    }

    @RequestMapping("/getParties")
    public List<Party> getPartiesWithMember(@RequestBody User member) {
        return partyService.getPartiesWithMemberId(member.getId());
    }

    @RequestMapping("/getPartiesWithOwner")
    public List<Party> getPartiesWithOwner(@RequestBody User owner) {
        return partyService.getPartiesWithOwnerId(owner.getId());
    }

    @RequestMapping("/addMember/{partyId}")
    public void addMember(@RequestBody User member, @PathVariable int partyId) {
        partyService.addMember(partyId, member);
    }

    @RequestMapping("/removeMember/{partyId}")
    public void removeMember(@RequestBody User member, @PathVariable int partyId) {
        partyService.removeMember(partyId, member);
    }

    @RequestMapping("/makePairings/{partyId}")
    public void makePairings(@PathVariable int partyId) {
        partyService.makePairings(partyId);
    }

    @RequestMapping("/getReceiver/{partyId}")
    public User getReceiver(@RequestBody User gifter, @PathVariable int partyId) {
        return partyService.getGiftReceiver(partyId, gifter);
    }

}
