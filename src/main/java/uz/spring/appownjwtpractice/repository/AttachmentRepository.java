package uz.spring.appownjwtpractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.spring.appownjwtpractice.entity.Attachment;
import uz.spring.appownjwtpractice.entity.AttachmentContent;

import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
