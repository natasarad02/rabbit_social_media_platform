package rs.ac.uns.ftn.informatika.jpa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.jpa.repository.ProfileRepository;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConcurrentProfileRegistrationTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    private final String TEST_USERNAME = "concurrent_user";

    @BeforeEach
    @AfterEach
    public void cleanup() {
        // This is a simplistic cleanup. In a real app, you might need a more robust way
        // to handle this without violating constraints if other tests depend on this user.
        profileRepository.deleteByUsername(TEST_USERNAME);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void testConcurrentRegistration() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // --- Task for Thread 1 ---
        Future<?> future1 = executor.submit(() -> {
            System.out.println("Thread 1: Attempting to create profile...");
            UserRequest request = new UserRequest(
                    null, // ID is null for a new user
                    TEST_USERNAME,
                    "password123",
                    "Concurrent",
                    "User",
                    "concurrent.user@example.com",
                    "123 Test Street"
            );
            profileService.createProfile(request);
        });

        // --- Task for Thread 2 ---
        Future<?> future2 = executor.submit(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            System.out.println("Thread 2: Attempting to create profile...");
            UserRequest request = new UserRequest(
                    null, // ID is null for a new user
                    TEST_USERNAME,
                    "password1234",
                    "Concurrent2",
                    "User2",
                    "concurrent.user2@example.com",
                    "1234 Test Street"
            );
            profileService.createProfile(request);
        });

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);
        Throwable exceptionCause = null;

        // Check the outcome of the first future
        try {
            future1.get();
            successCount.incrementAndGet();
            System.out.println("Future 1: SUCCESSFUL");
        } catch (ExecutionException e) {
            exceptionCause = e.getCause();
            if (exceptionCause instanceof ResourceConflictException) {
                conflictCount.incrementAndGet();
            }
            System.out.println("Future 1: FAILED with " + exceptionCause.getClass().getSimpleName());
        }

        // Check the outcome of the second future
        try {
            future2.get();
            successCount.incrementAndGet();
            System.out.println("Future 2: SUCCESSFUL");
        } catch (ExecutionException e) {
            exceptionCause = e.getCause();
            if (exceptionCause instanceof ResourceConflictException) {
                conflictCount.incrementAndGet();
            }
            System.out.println("Future 2: FAILED with " + exceptionCause.getClass().getSimpleName());
        }

        System.out.println("Final outcome: Successes=" + successCount.get() + ", Conflicts=" + conflictCount.get());

        // JUNIT 5: Assertions are from org.junit.jupiter.api.Assertions
        assertEquals(1, successCount.get(), "Exactly one registration should succeed.");
        assertEquals(1, conflictCount.get(), "Exactly one registration should fail with a conflict.");

        // Final proof: verify database state
        long userCount = profileRepository.countActiveByUsername(TEST_USERNAME);
        assertEquals(1, userCount, "Database should contain exactly one user with the test username.");

        executor.shutdown();
    }
}

