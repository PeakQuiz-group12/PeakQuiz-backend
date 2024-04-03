package idatt2105.peakquizbackend.mappers;

import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDTO toDTO(User user);
}
