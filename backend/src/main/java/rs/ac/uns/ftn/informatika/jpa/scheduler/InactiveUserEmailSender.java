package rs.ac.uns.ftn.informatika.jpa.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InactiveUserEmailSender {

    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public InactiveUserEmailSender(@Autowired ProfileRepository profileRepository, @Autowired PostRepository postRepository) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }

    @Scheduled(cron = "0 52 10 * * ?")
    public void sendEmailToInactiveUser()
    {
        List<Profile> users = profileRepository.findAllActiveProfiles();
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for(Profile user : users)
        {
            if(isInactiveForSevenDays(user.getLastActiveDate()))
            {
                int newPostsCount = postRepository.countPostsInLastSevenDays(user.getId(), sevenDaysAgo);
                String emailContent = "You haven't been active in the past 7 days. Here is what you have missed:\n\n";
                if (newPostsCount > 0) {
                    emailContent += "You have missed " + newPostsCount + " new posts in the last 7 days.\n";
                } else {
                    emailContent += "You haven't missed any new posts in the last 7 days.\n";
                }
                emailContent += "\nStay active to keep engaging with your followers!";
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Profile activity");
                mailMessage.setFrom(fromEmail);
                mailMessage.setText(emailContent);
                mailSender.send(mailMessage);
            }

        }

    }

    private boolean isInactiveForSevenDays(Timestamp lastActiveDate)
    {

        Instant now = Instant.now();
        Instant lastActiveInstant = lastActiveDate.toInstant();
        Duration duration = Duration.between(lastActiveInstant, now);
        return duration.toDays() >= 7;

    }
}
