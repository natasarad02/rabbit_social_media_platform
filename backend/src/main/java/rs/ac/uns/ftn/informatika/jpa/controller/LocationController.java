package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.LocationDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.StudentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.LocationDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Student;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;
import rs.ac.uns.ftn.informatika.jpa.service.LocationService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/locations")
public class LocationController {

    private LocationService locationService;
    private LocationDTOMapper locationDTOMapper;

    public LocationController(@Autowired LocationService locationService) {
      this.locationService = locationService;
    }

    @PostMapping("/savelocation")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        Location location = locationDTOMapper.fromDTOtoLocation(locationDTO);

        if(location == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        location = locationService.save(location);
        return new ResponseEntity<>(new LocationDTO(location), HttpStatus.OK);

    }

}
