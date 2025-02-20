package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatMessageDTO;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.service.ChatService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ChatContoller {
    private SimpMessagingTemplate template;
    private ChatService chatService;
    private ProfileService profileService;

    public  ChatContoller(@Autowired SimpMessagingTemplate template, @Autowired ChatService chatService, @Autowired ProfileService profileService) {
        this.template = template;
        this.chatService = chatService;
        this.profileService = profileService;
    }

    @MessageMapping("/send") // Klijent šalje poruke na /socket-subscriber/send
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDto) {

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatMessageDto.getMessage());
        chatMessage.setSender(profileService.findOne(chatMessageDto.getSender().getId()));
        chatMessage.setTimestamp(LocalDateTime.now());

        if (chatMessageDto.getReceiver() != null) {
            chatMessage.setReceiver(profileService.findOne(chatMessageDto.getReceiver().getId()));
        }
        else chatMessage.setReceiver(null);

        if (chatMessageDto.getChatGroup() != null) {
            chatMessage.setChatGroup(chatService.findGroup(chatMessageDto.getChatGroup().getId()));
            }
        else chatMessage.setChatGroup(null);


        ChatMessage savedMessage = chatService.saveMessage(chatMessage);

            // Šaljemo poruku klijentima
        if (chatMessage.getReceiver() != null) {
            template.convertAndSendToUser(
                    chatMessage.getReceiver().getEmail(), // Unikatni identifikator korisnika (može biti ID ili email)
                    "/queue/messages",
                    savedMessage
            );
        } else {
            // Grupna poruka, šalje se svim klijentima
            template.convertAndSend("/socket-publisher/messages", savedMessage);
        }

        return chatMessageDto;

    }


//    @GetMapping("/group/{groupId}/last10")
//    public List<ChatMessageDTO> getLast10Messages(@PathVariable Integer groupId) {
//        return chatService.getLastMessagesFromGroup(groupId)
//                .stream()
//                .map(ChatMessageDto::new)
//                .collect(Collectors.toList());
//    }
}
