package uz.spring.appownjwtpractice.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.dto.RegisterDto;
import uz.spring.appownjwtpractice.dto.LoginDto;
import uz.spring.appownjwtpractice.service.RegisterService;

@RestController
@RequestMapping("/api/user")
public class ApiRegisterController {

    final RegisterService userService;

    public ApiRegisterController(RegisterService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public HttpEntity<?> userRegister(@RequestBody RegisterDto registerDto) {
        Response response = userService.userRegister(registerDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode) {
        Response response = userService.verifyEmail(email, emailCode);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> userLogin(@RequestBody LoginDto loginDto) {
        Response response = userService.userLogin(loginDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
