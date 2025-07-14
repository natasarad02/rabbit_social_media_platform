package rs.ac.uns.ftn.informatika.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;import org.junit.Before;import org.springframework.orm.ObjectOptimisticLockingFailureException;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaExampleApplicationTests {


	@Autowired
	private ProfileService profileService;

    @Autowired
	private PostService postService;

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testOptimisticLockingFollowingScenario() throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {



			@Override
			public void run() {
				System.out.println("Startovan Thread 1");
				Profile profileToFollow = profileService.findOne(5); // Profil koji će oba korisnika pokušati da zaprate
				profileService.followProfile(6, 5); // Korisnik sa ID 6 prati profil sa ID 5
				try { Thread.sleep(3000); } catch (InterruptedException e) {}
				profileService.save(profileToFollow);// Pauza od 3 sekunde pre nego što se završi transakcija
			}
		});
		executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");
				Profile profileToFollow = profileService.findOne(5); // Isti profil kao i u prvom threadu
				profileService.followProfile(8, 5);
				profileService.save(profileToFollow);// Korisnik sa ID 8 prati profil sa ID 5
			}
		});

		try {
			future1.get(); // Hvata Exception iz prvog threada
		} catch (ExecutionException e) {
			System.out.println("Exception from thread: " + e.getCause().getClass()); // ObjectOptimisticLockingFailureException
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();

	}

	@Test
	public void contextLoads() {
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testOptimisticLockingLikingScenario() throws Throwable {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<?> future1 = executor.submit(new Runnable() {



			@Override
			public void run() {
				System.out.println("Startovan Thread 1");
				Post postToLike = postService.findOne(1); // Post koji će oba korisnika pokušati da lajkuju
				postService.addLike(4, 1);
				try { Thread.sleep(3000); } catch (InterruptedException e) {}
				postService.save(postToLike);// Pauza od 3 sekunde pre nego što se završi transakcija
			}
		});
		executor.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("Startovan Thread 2");
				Post postToLike = postService.findOne(1); // Isti post kao i u prvom threadu
				postService.addLike(7, 1);
				postService.save(postToLike);
			}
		});

		try {
			future1.get(); // Hvata Exception iz prvog threada
		} catch (ExecutionException e) {
			System.out.println("Exception from thread: " + e.getCause().getClass()); // ObjectOptimisticLockingFailureException
			throw e.getCause();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();


	}

}
