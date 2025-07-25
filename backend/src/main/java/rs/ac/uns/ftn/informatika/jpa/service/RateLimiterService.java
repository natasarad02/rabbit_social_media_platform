package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final long TIME_WINDOW = 60_000;
    private final Map<Integer, UserRequestInfo> requestCounts = new ConcurrentHashMap<>();

    public boolean isAllowed(Integer idProfile) {

        long now = Instant.now().toEpochMilli();
        requestCounts.putIfAbsent(idProfile, new UserRequestInfo());
        synchronized (requestCounts.get(idProfile)) {
            UserRequestInfo info = requestCounts.get(idProfile);


            info.requests.removeIf(timestamp -> now - timestamp > TIME_WINDOW);

            if (info.requests.size() < MAX_REQUESTS_PER_MINUTE) {
                info.requests.add(now);
                return true;
            } else {
                return false;
            }
        }

    }

    private static class UserRequestInfo {
        private final java.util.List<Long> requests = new java.util.ArrayList<>();
    }

}
