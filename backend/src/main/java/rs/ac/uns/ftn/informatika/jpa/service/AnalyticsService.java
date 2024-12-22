package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.AnalyticsDTO;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import java.time.LocalDate;

@Service
public class AnalyticsService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private ProfileRepository profileRepository;

    public  AnalyticsService(@Autowired PostRepository postRepository, @Autowired CommentRepository commentRepository, @Autowired ProfileRepository profileRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.profileRepository = profileRepository;
    }

    public AnalyticsDTO getAnalyticsData() {
        long weeklyPosts = postRepository.countAllByPostedTimeBetween(LocalDate.now().minusWeeks(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));
        long monthlyPosts = postRepository.countAllByPostedTimeBetween(LocalDate.now().minusMonths(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));
        long yearlyPosts = postRepository.countAllByPostedTimeBetween(LocalDate.now().minusYears(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));

        long weeklyComments = commentRepository.countAllByCommentedTimeBetween(LocalDate.now().minusWeeks(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));
        long monthlyComments = commentRepository.countAllByCommentedTimeBetween(LocalDate.now().minusMonths(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));
        long yearlyComments = commentRepository.countAllByCommentedTimeBetween(LocalDate.now().minusYears(1).atStartOfDay(), LocalDate.now().atTime(23, 59, 59, 999999999));

        long totalProfiles = profileRepository.countAllByDeleted(false);
        long profilesWithPosts = profileRepository.countProfilesWithPosts();
        long profilesWithComments = profileRepository.countProfilesWithCommentWithoutPosts();
        long profilesWithNoActivity = totalProfiles - (profilesWithPosts + profilesWithComments);

        double postPercentage = (double) profilesWithPosts / totalProfiles * 100;
        double commentOnlyPercentage = (double) profilesWithComments / totalProfiles * 100;
        double noActivityPercentage = (double) profilesWithNoActivity / totalProfiles * 100;

        AnalyticsDTO analyticsDTO = new AnalyticsDTO();
        analyticsDTO.setWeeklyPostsCount(weeklyPosts);
        analyticsDTO.setMonthlyPostsCount(monthlyPosts);
        analyticsDTO.setYearlyPostsCount(yearlyPosts);
        analyticsDTO.setWeeklyCommentsCount(weeklyComments);
        analyticsDTO.setMonthlyCommentsCount(monthlyComments);
        analyticsDTO.setYearlyCommentsCount(yearlyComments);
        analyticsDTO.setPostPercentage(postPercentage);
        analyticsDTO.setCommentOnlyPercentage(commentOnlyPercentage);
        analyticsDTO.setNoActivityPercentage(noActivityPercentage);

        return analyticsDTO;
    }



}
