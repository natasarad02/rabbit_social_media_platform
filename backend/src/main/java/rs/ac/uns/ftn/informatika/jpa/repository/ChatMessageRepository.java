package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findTop10ByChatGroupIdOrderByTimestampDesc(Integer chatGroupId);

}
