package db.server.party;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PartyRepository extends CrudRepository<Party, Integer> {
    List<Party> findByOwner_Id(int ind);
}
