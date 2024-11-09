package rs.ac.uns.ftn.informatika.jpa.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.LocationDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.primer.TeacherDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.primer.Teacher;

@Component
public class LocationDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public LocationDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Location fromDTOtoLocation(LocationDTO dto) {
        return modelMapper.map(dto, Location.class);
    }

    public static LocationDTO fromLocationToDTO(Location dto) {
        return modelMapper.map(dto, LocationDTO.class);
    }

}
