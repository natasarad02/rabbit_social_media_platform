package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;

import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {

    @Query("SELECT cg FROM ChatGroup cg LEFT JOIN FETCH cg.members WHERE cg.deleted = false")
    List<ChatGroup> findAllWithMembers();


}
