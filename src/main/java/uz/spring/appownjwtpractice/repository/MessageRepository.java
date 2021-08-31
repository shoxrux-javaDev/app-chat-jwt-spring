package uz.spring.appownjwtpractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.spring.appownjwtpractice.entity.Message;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {


    List<Message> findByChat_Id(UUID chat_id);

    Optional<Message> findByChat_IdAndIdAndChat_FromUser_Id(UUID chat_id, UUID id, UUID chat_fromUser_id);

     void deleteByChat_IdAndIdAndChat_FromUser_Id(UUID chat_id, UUID id, UUID chat_fromUser_id);
}
