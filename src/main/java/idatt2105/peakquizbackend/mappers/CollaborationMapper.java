package idatt2105.peakquizbackend.mappers;

import idatt2105.peakquizbackend.dto.CollaborationDTO;
import idatt2105.peakquizbackend.model.Collaboration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CollaborationMapper {

  CollaborationMapper INSTANCE = Mappers.getMapper(CollaborationMapper.class);

  CollaborationDTO toDTO(Collaboration collaboration);

}
