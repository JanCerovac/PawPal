package root.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import root.database.entites.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
    long countByRecipientIdAndReadAtIsNull(String recipientId);
}
