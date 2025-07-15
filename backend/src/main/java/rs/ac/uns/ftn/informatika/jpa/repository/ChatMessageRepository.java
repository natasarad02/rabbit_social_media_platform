package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findTop10ByChatGroupIdOrderByTimestampDesc(Integer chatGroupId);

    @Query("SELECT m FROM ChatMessage m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.timestamp ASC")
    List<ChatMessage> findAllBySenderAndReceiver(Integer senderId, Integer receiverId);

    List<ChatMessage> findAllByChatGroupIdOrderByTimestampAsc(Integer chatGroupId);

    List<ChatMessage> findMessagesByChatGroupIdAndTimestampGreaterThan(Integer groupId, LocalDateTime joinDate);

}
