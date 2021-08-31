package uz.spring.appownjwtpractice.service;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.spring.appownjwtpractice.dto.MessageDto;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.entity.*;
import uz.spring.appownjwtpractice.repository.*;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional(rollbackOn = NullPointerException.class)
public class MessageService {

    final ChatRepository chatRepository;
    final MessageRepository messageRepository;
    final UserRepository userRepository;
    final AttachmentRepository attachmentRepository;

    public MessageService(ChatRepository chatRepository,
                          MessageRepository messageRepository,
                          UserRepository userRepository,
                          AttachmentRepository attachmentRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }


    public Response addFileWithMessage(MessageDto messageDto) {
        Integer counter = 1;
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = fromUser.getId();
        Optional<Chat> chatOptional = chatRepository.findByFromUser_IdAndToUser_IdOrToUser_IdAndFromUser_Id(
                userId, messageDto.getReceiverId(),
                messageDto.getReceiverId(), userId);
        if (chatOptional.isEmpty()) {
            Optional<User> toUserOptional = userRepository.findById(messageDto.getReceiverId());
            if (toUserOptional.isEmpty()) return new Response("receiver not found", false);
            Chat chat = new Chat();
            chat.setFromUser(fromUser);
            chat.setToUser(toUserOptional.get());
            chatRepository.save(chat);
            Message message = new Message();
            if (messageDto.getMessage() != (null))
                message.setMessage(messageDto.getMessage());
            if (messageDto.getAttachmentId() == null) {
                return new Response("messages not be null", false);
            }
            Optional<Attachment> attachmentOptional = attachmentRepository.findById(messageDto.getAttachmentId());
            if (attachmentOptional.isEmpty()) return new Response("file not found", false);
            message.setAttachment(attachmentOptional.get());
            message.setChat(chat);
            message.setStatus(true);
            message.setCounter(counter);
            messageRepository.save(message);
            return new Response("first message saved", true);
        } else {
            List<Message> messageOptional = messageRepository.findByChat_Id(chatOptional.get().getId());
            if (messageOptional.isEmpty()) return new Response("error", false);
            Message message = new Message();
            message.setMessage(messageDto.getMessage());
            if (messageDto.getAttachmentId() != null) {
                Optional<Attachment> attachmentOptional = attachmentRepository.findById(messageDto.getAttachmentId());
                if (attachmentOptional.isEmpty()) return new Response("file not found", false);
                message.setAttachment(attachmentOptional.get());
            }
            message.setChat(chatOptional.get());
            message.setStatus(true);
            messageOptional.stream().
                    filter(thisMessage -> thisMessage.getChat().getId() == chatOptional.get().getId())
                    .map(thisMessage -> thisMessage.getCounter() + counter).forEachOrdered(message::setCounter);
            messageRepository.save(message);
            return new Response("message saved", true, message.getId());
        }
    }

    public Response editMessage(UUID id, MessageDto messageDto) {
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID fromUserId = fromUser.getId();
        Optional<Chat> chatOptional = chatRepository.findByFromUser_IdAndToUser_IdOrToUser_IdAndFromUser_Id(
                fromUserId, messageDto.getReceiverId(),
                messageDto.getReceiverId(), fromUserId);
        if (chatOptional.isEmpty()) return new Response("chat not found", false);
        Optional<Message> messageOptional = messageRepository.findByChat_IdAndIdAndChat_FromUser_Id(
                chatOptional.get().getId(),
                id,
                fromUserId
        );
        if (messageOptional.isEmpty()) return new Response("message not found", false);
        Message message = messageOptional.get();
        message.setMessage(messageDto.getMessage());
        return new Response("message edited", true);
    }

    public Response getMessage(UUID receiverId) {
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = fromUser.getId();
        Optional<Chat> chatOptional = chatRepository.findByFromUser_IdAndToUser_IdOrToUser_IdAndFromUser_Id(
                id, receiverId,
                receiverId, id);
        if (chatOptional.isEmpty()) return new Response("chat not found", false);
        List<Message> messageList = messageRepository.findByChat_Id(chatOptional.get().getId());
        messageRepository.findByChat_Id(chatOptional.get().getId());
        if (messageList.isEmpty()) return new Response("message not found", false);
        for (Message message : messageList) {
            if (message.getChat().equals(chatOptional.get())) {
                message.setCounter(0);
                message.setStatus(false);
            }
        }
        return new Response("successfully found messages", true, messageList);
    }

    public Response deleteMessage(UUID id, MessageDto messageDto) {
        User frUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID frUserId = frUser.getId();
        Optional<Chat> chatOptional = chatRepository.findByFromUser_IdAndToUser_IdOrToUser_IdAndFromUser_Id(
                frUserId, messageDto.getReceiverId(),
                messageDto.getReceiverId(), frUserId);
        if (chatOptional.isEmpty()) return new Response("chat not found", false);
        messageRepository.deleteByChat_IdAndIdAndChat_FromUser_Id(chatOptional.get().getId(), id, frUserId);
        return new Response("message deleted", true);
    }
}
