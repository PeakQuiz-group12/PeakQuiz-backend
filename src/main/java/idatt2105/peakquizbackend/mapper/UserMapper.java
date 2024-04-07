package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting between User entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert
     * @return The corresponding UserDTO
     */
    UserDTO toDTO(User user);
}
