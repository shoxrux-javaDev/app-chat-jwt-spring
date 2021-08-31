package uz.spring.appownjwtpractice.service;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.spring.appownjwtpractice.dto.RegisterDto;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.entity.User;
import uz.spring.appownjwtpractice.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response getAccount() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        UUID userId = user.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) return new Response("error", false);
        User fromUser = userOptional.get();
        return new Response("success", true, fromUser);
    }

    public Response editInformation(RegisterDto registerDto) {
        User editUser = (User) SecurityContextHolder.getContext().getAuthentication();
        UUID id = editUser.getId();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return new Response("not found user account", false);
        User editingUserInfo = userOptional.get();
        editingUserInfo.setFirstname(registerDto.getFirstname());
        editingUserInfo.setLastname(registerDto.getLastname());
        editingUserInfo.setEmail(registerDto.getEmail());
        editingUserInfo.setPhoneNumber(registerDto.getPhoneNumber());
        editingUserInfo.setPassword(registerDto.getPassword());
        userRepository.save(editingUserInfo);
        return new Response("success with user",true);
    }

}
