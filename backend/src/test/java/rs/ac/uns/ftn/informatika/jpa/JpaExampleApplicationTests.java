package rs.ac.uns.ftn.informatika.jpa;

// --- JUNIT 5 IMPORTS ---
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

// --- SPRING IMPORTS ---
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension; // JUnit 5 extension
import org.springframework.orm.ObjectOptimisticLockingFailureException;

// --- YOUR DOMAIN IMPORTS ---
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;

// --- OTHER JAVA IMPORTS ---
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


// Use the JUnit 5 extension for Spring
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JpaExampleApplicationTests {

	@Autowired
	private ProfileService profileService;

	@Autowired
	private PostService postService;

	@Test
	public void testOptimisticLockingFollowingScenario() {
		// Use assertThrows to verify that the specific exception is thrown
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
			ExecutorService executor = Executors.newFixedThreadPool(2);
			try {
				Future<?> future1 = executor.submit(() -> {
					System.out.println("Startovan Thread 1");
					// In a real test, you should not rely on hardcoded IDs.
					// It's better to create test data before the test runs.
					Profile profileToFollow = profileService.findOne(5);
					profileService.followProfile(6, 5);
					try {
						Thread.sleep(3000); // Sleep to create a race condition
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					profileService.save(profileToFollow);
				});

				executor.submit(() -> {
					System.out.println("Startovan Thread 2");
					Profile profileToFollow = profileService.findOne(5);
					profileService.followProfile(8, 5);
					profileService.save(profileToFollow);
				});

				try {
					future1.get(); // This will re-throw the exception from the thread
				} catch (ExecutionException e) {
					System.out.println("Exception from thread: " + e.getCause().getClass());
					// Unwrap the original exception and throw it so assertThrows can catch it
					if (e.getCause() != null) {
						throw e.getCause();
					}
				}
			} finally {
				executor.shutdown();
			}
		});
	}

	@Test
	public void contextLoads() {
		// This test remains the same, just uses the new @Test annotation from JUnit 5
	}

	@Test
	public void testOptimisticLockingLikingScenario() {
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
			ExecutorService executor = Executors.newFixedThreadPool(2);
			try {
				Future<?> future1 = executor.submit(() -> {
					System.out.println("Startovan Thread 1");
					Post postToLike = postService.findOne(1);
					postService.addLike(4, 1);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					postService.save(postToLike);
				});

				executor.submit(() -> {
					System.out.println("Startovan Thread 2");
					Post postToLike = postService.findOne(1);
					postService.addLike(7, 1);
					postService.save(postToLike);
				});

				try {
					future1.get();
				} catch (ExecutionException e) {
					System.out.println("Exception from thread: " + e.getCause().getClass());
					if (e.getCause() != null) {
						throw e.getCause();
					}
				}
			} finally {
				executor.shutdown();
			}
		});
	}
}