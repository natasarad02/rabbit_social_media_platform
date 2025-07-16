package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.dto.RabbitLocationMessageDTO;
import rs.ac.uns.ftn.informatika.jpa.model.RabbitLocation;
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitLocationRepository;

import java.util.List;

@Service
public class RabbitLocationService {

    private final RabbitLocationRepository rabbitLocationRepository;

    @Autowired
    public RabbitLocationService(RabbitLocationRepository rabbitLocationRepository) {
        this.rabbitLocationRepository = rabbitLocationRepository;
    }

    @Transactional
    public RabbitLocation saveLocation(RabbitLocationMessageDTO locationDto) {
        RabbitLocation locationEntity = new RabbitLocation(
                locationDto.getId(),
                locationDto.getName(),
                locationDto.getLocationData().getLatitude(),
                locationDto.getLocationData().getLongitude()
        );

        RabbitLocation savedLocationEntity = rabbitLocationRepository.save(locationEntity);
        System.out.println("Saved vet/clinic/shelter:" + savedLocationEntity.getId());

        return savedLocationEntity;
    }

    public List<RabbitLocation> getAllLocations() {
        return rabbitLocationRepository.findAll();
    }

    public RabbitLocation getLocationById(String id) {
        return rabbitLocationRepository.findById(id).orElse(null);
    }
}
