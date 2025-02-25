package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatGroupDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatGroupMemberDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatMessageDTO;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroupMember;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.service.ChatService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/chat")
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
        chatMessage.setSender(profileService.findOne(chatMessageDto.getSender()));
        chatMessage.setTimestamp(LocalDateTime.now());

        if (chatMessageDto.getReceiver() != null) {
            chatMessage.setReceiver(profileService.findOne(chatMessageDto.getReceiver()));
        }
        else chatMessage.setReceiver(null);

        if (chatMessageDto.getChatGroup() != null) {
            chatMessage.setChatGroup(chatService.findGroup(chatMessageDto.getChatGroup()));
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

    @PreAuthorize("hasAuthority('User')")
    @GetMapping
    public List<ChatMessageDTO> getMessages(@RequestParam Integer senderId, @RequestParam Integer receiverId) {

        List<ChatMessage> messages = chatService.findAllMessagesFromSenderAndReceiver(senderId, receiverId);
        return messages.stream()
                .map(msg -> new ChatMessageDTO(
                        msg.getId(),
                        msg.getMessage(),
                        msg.getSender().getId(),
                        msg.getReceiver() != null ? msg.getReceiver().getId() : -1, // Set receiver ID to -1 if null
                        msg.getChatGroup() != null ? msg.getChatGroup().getId() : -1, // Set chatGroup ID to -1 if null
                        msg.getTimestamp() // Assuming timestamp is `LocalDateTime`
                ))
                .collect(Collectors.toList());
    }

    // Endpoint to get all groups where the user is an admin or a member

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatGroupDTO>> getGroupsForUser(@PathVariable Integer userId) {
        List<ChatGroup> groups = chatService.findAllGroupsFromAdminOrMember(userId);
        if(groups.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ChatGroupDTO> groupDTOs = groups.stream()
                .map(grp -> new ChatGroupDTO(
                        grp.getId(),
                        grp.getName(),
                        grp.getAdmin() != null ? grp.getAdmin().getId() : -1,
                        new ArrayList<>() // Collecting IDs
                ))
                .collect(Collectors.toList());

        List<ChatGroupMember> chatGroupMembers = chatService.findAllMembers();

        List<ChatGroupMemberDTO> chatGroupMemberDTOs = chatGroupMembers.stream()
                .map(m -> new ChatGroupMemberDTO(
                        m.getId(),
                        m.getChatGroup().getId(),
                        m.getProfile().getId(),
                        m.getJoinDate()
                )).collect(Collectors.toList());

        for (ChatGroupDTO cgDTO : groupDTOs) {
            List<Integer> pom = new ArrayList<>();
            for (ChatGroupMemberDTO cgmDTO : chatGroupMemberDTOs) {
                if(cgmDTO.getChatGroupId().equals(cgDTO.getId())) {
                    pom.add(cgmDTO.getProfile());
                }
            }
            cgDTO.setMembers(pom);
        }

        return new ResponseEntity<>(groupDTOs, HttpStatus.OK);
    }



    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/{groupId}/messages/{userId}")
    public ResponseEntity<List<ChatMessageDTO>> getAllMessagesForUser(
            @PathVariable Integer userId,
            @PathVariable Integer groupId) {

        List<ChatMessage> messages = chatService.findAllMessagesInGroupForUser(userId, groupId);
        List<ChatMessageDTO> messageDTOS = messages.stream()
                .map(msg -> new ChatMessageDTO(
                        msg.getId(),
                        msg.getMessage(),
                        msg.getSender().getId(),
                        msg.getReceiver() != null ? msg.getReceiver().getId() : -1, // Set receiver ID to -1 if null
                        msg.getChatGroup() != null ? msg.getChatGroup().getId() : -1, // Set chatGroup ID to -1 if null
                        msg.getTimestamp() // Assuming timestamp is `LocalDateTime`
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(messageDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/createGroup")
    public ResponseEntity<Void> createGroup(
            @RequestParam Integer userId,
            @RequestParam String groupName) {
        try {
            ChatGroup newGroup = chatService.createGroup(userId, groupName);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/{groupId}/add-member/{userId}")
    public ResponseEntity<Void> addMemberToGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        try {
            String response = chatService.addMemberToGroup(groupId, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAuthority('User')")
    @DeleteMapping("/{groupId}/remove-member/{userId}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        try {
        String response = chatService.removeMemberFromGroup(groupId, userId);
        return ResponseEntity.ok().build();} catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }



}
