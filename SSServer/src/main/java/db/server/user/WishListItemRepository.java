package db.server.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WishListItemRepository extends CrudRepository<WishListItem, Integer> {
    List<WishListItem> findByOwner_Id(int id);
}
