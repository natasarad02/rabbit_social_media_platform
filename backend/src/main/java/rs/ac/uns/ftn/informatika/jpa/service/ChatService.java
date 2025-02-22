package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroupMember;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatGroupMemberRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatGroupRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatMessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {
    private ChatMessageRepository chatMessageRepository;
    private ChatGroupRepository chatGroupRepository;
    private ChatGroupMemberRepository chatGroupMemberRepository;

    public ChatService(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatGroupRepository chatGroupRepository, @Autowired ChatGroupMemberRepository chatGroupMemberRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.chatGroupMemberRepository = chatGroupMemberRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getLastMessagesFromGroup(Integer groupId) {
        return chatMessageRepository.findTop10ByChatGroupIdOrderByTimestampDesc(groupId);
    }

    public ChatGroup findGroup(Integer id) {
        return chatGroupRepository.findById(id).orElseGet(null);
    }

    public List<ChatMessage> findAllMessagesFromSenderAndReceiver(Integer senderId, Integer receiverId) {
        return chatMessageRepository.findAllBySenderAndReceiver(senderId, receiverId);
    }

    public List<ChatMessage> findAllMessagesInGroupForUser(Integer userId, Integer groupId) {
        ChatGroup chatGroup = chatGroupRepository.findById(groupId).orElseGet(null);
        List<ChatMessage> messages = new ArrayList<>();

        if(chatGroup == null) {
            return new ArrayList<>();
        }

        if (chatGroup.getAdmin().getId().equals(userId)) {
            // If the user is the admin, return all messages in the group
            return chatMessageRepository.findAllByChatGroupIdOrderByTimestampAsc(groupId);
        }

        ChatGroupMember isMember = chatGroupMemberRepository.findByChatGroupIdAndProfileId(groupId, userId);
        if(isMember == null) {
            return new ArrayList<>();
        }
        else {
            LocalDateTime joinDate = isMember.getJoinDate();

            List<ChatMessage> lastMessages = chatMessageRepository.findTop10ByChatGroupIdOrderByTimestampDesc(groupId);

            // Step 4: Get the messages sent after the join date in chronological order
            List<ChatMessage> memberMessages = chatMessageRepository.findMessagesByChatGroupIdAndTimestampGreaterThan(groupId, joinDate);

            // Step 5: Combine both lists
            messages.addAll(lastMessages); // Add last 10 messages first
            messages.addAll(memberMessages); // Add messages after the join date

            // Step 6: Sort the final list so that all messages are in correct chronological order
            messages.sort(Comparator.comparing(ChatMessage::getTimestamp));

        }

        return messages;



    }

    public List<ChatGroup> findAllGroupsFromAdminOrMember(Integer userId) {
        return chatGroupRepository.findAllGroupsFromAdminOrMember(userId);
    }
}
