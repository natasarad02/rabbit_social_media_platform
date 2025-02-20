package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {
}
