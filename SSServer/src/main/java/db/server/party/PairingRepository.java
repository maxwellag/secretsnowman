package db.server.party;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PairingRepository extends CrudRepository<Pairing, Integer> {
    List<Pairing> findByGifter_Id(int id);
    List<Pairing> findByReceiver_Id(int id);
    List<Pairing> findByParty_Id(int id);
}
