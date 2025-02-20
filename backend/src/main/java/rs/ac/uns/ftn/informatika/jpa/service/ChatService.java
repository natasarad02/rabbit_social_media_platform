package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatGroupRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatMessageRepository;

import java.util.List;

@Service
public class ChatService {
    private ChatMessageRepository chatMessageRepository;
    private ChatGroupRepository chatGroupRepository;

    public ChatService(@Autowired ChatMessageRepository chatMessageRepository, @Autowired ChatGroupRepository chatGroupRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatGroupRepository = chatGroupRepository;
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
}
