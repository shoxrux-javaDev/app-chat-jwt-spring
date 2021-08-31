package uz.spring.appownjwtpractice.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.spring.appownjwtpractice.dto.RegisterDto;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.service.UserService;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    final UserService userService;

    public ApiUserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize(value = "hasAnyAuthority('GET_OWN_INFORMATION')")
    @GetMapping("/getAccount")
    public HttpEntity<?> getUserInformation() {
        Response response = userService.getAccount();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('EDIT_OWN_INFORMATION')")
    @PutMapping("/editAccount")
    public HttpEntity<?> editUserInformation(@RequestBody RegisterDto registerDto) {
        Response response = userService.editInformation(registerDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    /*@PreAuthorize(value = "hasAnyAuthority('GET_ONLINE_USERS','ADMIN')")
    @GetMapping("/onlineUser")
    public HttpEntity<?> getOnline() {
        Response response = userService.getOnline();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }*/
}
