package uz.spring.appownjwtpractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.spring.appownjwtpractice.entity.Chat;
import uz.spring.appownjwtpractice.entity.Message;
import uz.spring.appownjwtpractice.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    Optional<Chat> findByFromUser_IdAndToUser_IdOrToUser_IdAndFromUser_Id(UUID frUserId, UUID toUserId,
                                                                          UUID toUserId2, UUID fromUserId2);

}
