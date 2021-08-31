package uz.spring.appownjwtpractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.spring.appownjwtpractice.entity.User;
import uz.spring.appownjwtpractice.enums.PermissionEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

    List<User> findAllByActive(boolean active);

}
