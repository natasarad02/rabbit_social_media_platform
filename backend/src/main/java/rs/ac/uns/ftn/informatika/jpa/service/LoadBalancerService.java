package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {

    // Lista dostupnih instanci aplikacije na koje ćemo preusmeravati zahteve
    private final List<String> instances = Arrays.asList(
            "http://localhost:8081",
            "http://localhost:8082"
    );

    // AtomicInteger se koristi za bezbedno menjanje vrednosti u konkurentnom okruženju
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    // RestTemplate omogućava slanje HTTP zahteva ka drugim serverima
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Prosleđuje HTTP GET zahtev nekoj od dostupnih instanci koristeći Round Robin algoritam.
     * Ako odabrana instanca nije dostupna, pokušava sledeću sve dok ne iscrpi sve instance.
     * Ako nijedna instanca nije dostupna, vraća HTTP 503 Service Unavailable.
     */
    public ResponseEntity<String> forwardRequest() {
        // Iteriramo onoliko puta koliko ima instanci, tj. pokušavamo svaku po jednom
        for (int i = 0; i < instances.size(); i++) {
            String targetInstance = getNextInstance(); // Dobijamo sledeću instancu na osnovu Round Robin algoritma
            try {
                // Prosleđujemo zahtev instanci
                return restTemplate.getForEntity(targetInstance + "/api/proxy/data", String.class);
            } catch (Exception e) {
                // Ako instanca nije dostupna, logujemo grešku i pokušavamo sledeću, retry algoritam
                System.out.println("Instanca nije dostupna: " + targetInstance);
            }
        }
        // Ako nijedna instanca nije dostupna, vraćamo HTTP 503 (Service Unavailable)
        return ResponseEntity.status(503).body("Sve instance su nedostupne!");
    }

    /**
     * Implementacija **Round Robin** algoritma za odabir sledeće instance.
     *
     * Round Robin je jedan od najjednostavnijih algoritama za load balancing.
     * On raspoređuje zahteve na servere **jedan po jedan u kružnom redosledu**.
     *
     * Primer ako imamo 3 servera:
     * - Prvi zahtev ide na **Server 1**
     * - Drugi zahtev ide na **Server 2**
     * - Treći zahtev ide na **Server 3**
     * - Četvrti zahtev ponovo ide na **Server 1**
     * - I tako u krug...
     */
    private String getNextInstance() {
        // Atomically dobijamo sledeći indeks i osiguravamo da bude unutar granica liste
        int index = currentIndex.getAndUpdate(i -> (i + 1) % instances.size());
        return instances.get(index); // Vraćamo instancu na odgovarajućem indeksu
    }
}
