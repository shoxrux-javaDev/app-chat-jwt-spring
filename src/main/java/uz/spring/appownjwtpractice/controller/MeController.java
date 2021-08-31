package uz.spring.appownjwtpractice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.spring.appownjwtpractice.entity.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/me")
public class MeController {

    @PreAuthorize(value = "hasAnyAuthority('GET_ONLINE_USERS')")
    @GetMapping
    public ResponseEntity<?> getLoggedUserInfo(Authentication authentication) {
        User user;
        Map<String, Object> result = new HashMap<>();
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
            result.put("id", user.getId());
            result.put("email", user.getEmail());
            result.put("roles", user.getAuthorities());
        }
        return ResponseEntity.ok(result);
    }
}
