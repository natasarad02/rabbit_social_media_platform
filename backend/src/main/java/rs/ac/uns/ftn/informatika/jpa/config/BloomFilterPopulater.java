package rs.ac.uns.ftn.informatika.jpa.config;

import com.google.common.hash.BloomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;

import java.util.stream.Stream;

@Component
public class BloomFilterPopulater {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterPopulater.class);

    private final ProfileRepository profileRepository;
    private final BloomFilter<String> userBloomFilter;

    public BloomFilterPopulater(ProfileRepository profileRepository, BloomFilter<String> userBloomFilter) {
        this.profileRepository = profileRepository;
        this.userBloomFilter = userBloomFilter;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void populateUserBloomFilter() {
        logger.info("Starting to populate user Bloom Filter from active profiles...");

        try (Stream<String> usernames = profileRepository.findAllActiveProfilesUsernames()) {
            usernames.forEach(userBloomFilter::put);
        }

        long count = userBloomFilter.approximateElementCount();
        logger.info("User Bloom Filter populated with approximately {} active usernames.", count);
    }
}