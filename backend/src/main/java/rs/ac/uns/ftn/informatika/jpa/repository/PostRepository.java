package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    List<Post> findAllByProfileId(@Param("profileId") Integer profileId);

    @Query("SELECT p FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    Page<Post> findAllByProfileId(@Param("profileId") Integer profileId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.profile.id = :profileId AND p.deleted = false")
    Integer countPostsForProfile(@Param("profileId") Integer profileId);
}
