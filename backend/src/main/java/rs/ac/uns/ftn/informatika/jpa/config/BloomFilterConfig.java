package rs.ac.uns.ftn.informatika.jpa.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class BloomFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(BloomFilterConfig.class);

    @Bean
    public BloomFilter<String> userBloomFilter(BloomFilterProperties properties) {
        logger.info("Defining User Bloom Filter with {} expected insertions and {} fpp",
                properties.getExpectedInsertions(), properties.getFpp());
        return BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                properties.getExpectedInsertions(),
                properties.getFpp()
        );
    }
}