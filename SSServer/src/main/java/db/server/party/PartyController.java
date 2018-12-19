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

    @RequestMapping("/make/{ownerId}/{partyName}")
    public Party makeParty(@RequestBody String[] description, @PathVariable int ownerId,
                           @PathVariable String partyName) {
        return partyService.makeParty(ownerId, partyName, description[0]);
    }

    @RequestMapping("/get/{partyId}")
    public Party getParty(@PathVariable int partyId) {
        return partyService.getParty(partyId);
    }

    @RequestMapping("/getParties")
    public List<Party> getPartiesWithMember(@RequestBody User member) {
        return partyService.getPartiesWithMemberId(member.getId());
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
