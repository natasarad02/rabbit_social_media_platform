package rs.ac.uns.ftn.informatika.jpa.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUsersMetricService {
    private final AtomicInteger activeUserCount = new AtomicInteger(0);

    public ActiveUsersMetricService(MeterRegistry meterRegistry) {
        meterRegistry.gauge("application.active_users", activeUserCount);
    }

    public void setActiveUserCount(int count) {
        activeUserCount.set(count);
    }
}
