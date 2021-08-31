package uz.spring.appownjwtpractice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.spring.appownjwtpractice.entity.Role;
import uz.spring.appownjwtpractice.enums.RoleName;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleName(RoleName user);

}
