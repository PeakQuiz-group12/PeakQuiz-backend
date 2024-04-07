package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.CollaborationDTO;
import idatt2105.peakquizbackend.model.Collaboration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Collaboration entities to CollaborationDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface CollaborationMapper {

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    CollaborationMapper INSTANCE = Mappers.getMapper(CollaborationMapper.class);

    /**
     * Converts a Collaboration entity to a CollaborationDTO.
     *
     * @param collaboration
     *            The Collaboration entity to convert
     * @return The corresponding CollaborationDTO
     */
    @Mapping(target = "userId", source = "collaboration.user.id")
    @Mapping(target = "quizId", source = "collaboration.quiz.id")
    CollaborationDTO toDTO(Collaboration collaboration);

}
