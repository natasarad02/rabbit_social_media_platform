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
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private ChatMessageRepository chatMessageRepository;
    private ChatGroupRepository chatGroupRepository;
    private ChatGroupMemberRepository chatGroupMemberRepository;
    private ProfileRepository profileRepository;

    public ChatService(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatGroupRepository chatGroupRepository, @Autowired ChatGroupMemberRepository chatGroupMemberRepository, @Autowired ProfileRepository profileRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.chatGroupMemberRepository = chatGroupMemberRepository;
        this.profileRepository = profileRepository;
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
        // Get groups where user is an admin
        List<ChatGroup> chatGroups = new ArrayList<>(chatGroupRepository.findAllByAdminId(userId));

        // Fetch only relevant group members instead of all members
        List<ChatGroupMember> chatGroupMembers = chatGroupMemberRepository.findAllByProfileId(userId);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: " + chatGroupMembers.size());

        for (ChatGroupMember chatGroupMember : chatGroupMembers) {
            ChatGroup chatGroup = chatGroupMember.getChatGroup();
            if (chatGroup != null && !chatGroups.contains(chatGroup)) {
                chatGroups.add(chatGroup);
            }
        }

        return chatGroups;
    }


    public List<ChatGroupMember> findAllMembers()
    {
        return chatGroupMemberRepository.findAll();
    }



    public String addMemberToGroup(Integer groupId, Integer userId) {
        ChatGroup chatGroup = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is already a member
        ChatGroupMember existingMember = chatGroupMemberRepository.findByChatGroupIdAndProfileId(groupId, userId);
        if (existingMember != null) {
            return "User is already a member of this group.";
        }

        // Add new member to group
        ChatGroupMember newMember = new ChatGroupMember();
        newMember.setChatGroup(chatGroup);
        newMember.setProfile(profile);
        newMember.setJoinDate(LocalDateTime.now()); // Set current date and time as join date

        chatGroupMemberRepository.save(newMember);
        return "User added to group successfully.";
    }

    public String removeMemberFromGroup(Integer groupId, Integer userId) {
        ChatGroup chatGroup = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Check if the user is a member of the group
        ChatGroupMember member = chatGroupMemberRepository.findByChatGroupIdAndProfileId(groupId, userId);
        if (member == null) {
            return "User is not a member of this group.";
        }

        // Remove member from group
        chatGroupMemberRepository.delete(member);
        return "User removed from group successfully.";
    }

    // Create a new group where the creator is the admin
    public ChatGroup createGroup(Integer creatorId, String groupName) {
        Profile creator = profileRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // Create new group
        ChatGroup newGroup = new ChatGroup();
        newGroup.setName(groupName);
        newGroup.setAdmin(creator);  // Set creator as admin
        newGroup.setDeleted(false);
        newGroup.setMembers(new ArrayList<>());
        newGroup.setMessages(new ArrayList<>());
        newGroup = chatGroupRepository.save(newGroup);

        return newGroup;
    }


    public List<ChatGroupMember> getMembersFromGroup(Integer groupId) {
        return chatGroupMemberRepository.findByChatGroupIdWithProfile(groupId);
    }
}
