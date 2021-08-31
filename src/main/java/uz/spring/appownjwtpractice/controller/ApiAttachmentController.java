package uz.spring.appownjwtpractice.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.service.AttachmentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class ApiAttachmentController {

    final AttachmentService attachmentService;

    public ApiAttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PreAuthorize(value = "hasAnyAuthority('ADD_MESSAGE')")
    @PostMapping("/attach")
    public HttpEntity<?> addMessage(@RequestParam List<MultipartFile> files) {
        Response response = attachmentService.addFiles(files);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response.getObject());
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_MESSAGE')")
    @GetMapping("/getAttach/{id}")
    public void getFile(@PathVariable UUID id, HttpServletResponse resp) {
        attachmentService.getFile(id, resp);
    }
}
