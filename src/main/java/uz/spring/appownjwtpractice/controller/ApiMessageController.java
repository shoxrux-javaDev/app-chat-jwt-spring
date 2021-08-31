package uz.spring.appownjwtpractice.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.spring.appownjwtpractice.dto.MessageDto;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.service.MessageService;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
public class ApiMessageController {

    final MessageService messageService;

    public ApiMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize(value = "hasAnyAuthority('ADD_MESSAGE')")
    @PostMapping("/addMessage")
    public HttpEntity<?> addMessage(@RequestBody MessageDto messageDto) {
        Response response = messageService.addFileWithMessage(messageDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_MESSAGE')")
    @GetMapping("/getMessage/{id}")
    public HttpEntity<?> getMessage(@PathVariable UUID id) {
        Response response = messageService.getMessage(id);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('EDIT_MESSAGE')")
    @PutMapping("/edit/{id}")
    public HttpEntity<?> editMessage(@PathVariable UUID id, @RequestBody MessageDto messageDto) {
        Response response = messageService.editMessage(id, messageDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADD_MESSAGE')")
    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteMessage(@PathVariable UUID id, @RequestBody MessageDto messageDto) {
        Response response = messageService.deleteMessage(id, messageDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
