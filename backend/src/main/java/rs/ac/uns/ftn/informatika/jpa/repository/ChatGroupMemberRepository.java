package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroup;
import rs.ac.uns.ftn.informatika.jpa.model.ChatGroupMember;
import rs.ac.uns.ftn.informatika.jpa.model.ChatMessage;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.util.List;

public interface ChatGroupMemberRepository extends JpaRepository<ChatGroupMember, Integer> {


    ChatGroupMember findByChatGroupIdAndProfileId(Integer chatGroupId, Integer profileId);

}
