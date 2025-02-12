package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBucketRateLimiterService {
    private static final int MAX_TOKENS = 5;
    private static final long REFILL_TIME_MILLIS = 12 * 1000;

    private final Map<Integer, UserBucket> userBuckets = new ConcurrentHashMap<>();

    public boolean isAllowed(Integer userId) {
        userBuckets.putIfAbsent(userId, new UserBucket(MAX_TOKENS, Instant.now().toEpochMilli()));
        return userBuckets.get(userId).consumeToken();
    }

    private static class UserBucket {
        private int tokens;
        private long lastRefillTime;

        public UserBucket(int tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }

        synchronized boolean consumeToken() {
            refillTokens();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refillTokens() {
            long currentTime = Instant.now().toEpochMilli();
            long elapsedTime = currentTime - lastRefillTime;
            int tokensToAdd = (int) (elapsedTime / REFILL_TIME_MILLIS);

            if (tokensToAdd > 0) {
                tokens = Math.min(MAX_TOKENS, tokens + tokensToAdd);
                lastRefillTime = currentTime;
            }
        }
    }
}
