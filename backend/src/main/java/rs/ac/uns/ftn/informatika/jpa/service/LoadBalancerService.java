package rs.ac.uns.ftn.informatika.jpa.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.config.LoadBalancerConfig;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Round-Robin algoritam znači da se svaki novi zahtev šalje na sledeću instancu iz liste
@Service
public class LoadBalancerService {
    private List<String> instances;
    private AtomicInteger currentIndex = new AtomicInteger(0); //pracenje indeksa kako bi slao zahteve na sledecu instancu

    public LoadBalancerService(@Autowired LoadBalancerConfig config) {
        this.instances = config.getInstances();
    }

    public String getNextInstance() {
        if (instances.isEmpty()) {
            throw new IllegalStateException("No backend instances available!");
        }
        int index = currentIndex.getAndUpdate(i -> (i + 1) % instances.size());
        return instances.get(index);
    }
}
