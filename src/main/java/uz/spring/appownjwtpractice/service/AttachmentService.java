package uz.spring.appownjwtpractice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.entity.*;
import uz.spring.appownjwtpractice.repository.AttachmentContentRepository;
import uz.spring.appownjwtpractice.repository.AttachmentRepository;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachmentService {

    final AttachmentRepository attachmentRepository;
    final AttachmentContentRepository attachmentContentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository,
                             AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    public Response addFiles(List<MultipartFile> file) {
        Attachment attachment = null;
        for (MultipartFile multipartFile : file) {
            if (multipartFile != null) {
                attachment = optimalMethodUpload(multipartFile);
            }
        }
        return new Response("files upload to Db", true, Objects.requireNonNull(attachment).getId());
    }

    public Attachment optimalMethodUpload(MultipartFile file) {
        try {
            Attachment savedAttachment = attachmentRepository.save(new Attachment(file.getOriginalFilename(),
                    file.getSize(), file.getContentType()));
            attachmentContentRepository.save(new AttachmentContent(file.getBytes(), savedAttachment));
            return savedAttachment;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getFile(UUID id, HttpServletResponse response) {
        try {
            Attachment attachment = attachmentRepository.getById(id);
            AttachmentContent content = attachmentContentRepository.findAllByAttachment_Id(id);
            response.setContentType(attachment.getContentType());
            response.setHeader("content-type", "File:" + attachment.getName());
            FileCopyUtils.copy(content.getBytes(), response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
