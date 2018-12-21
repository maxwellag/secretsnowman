package db.server.notification;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    List<Notification> findByOwner_Id(int id);
}
