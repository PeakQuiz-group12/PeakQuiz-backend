package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuestionCreateDTO;
import idatt2105.peakquizbackend.dto.QuestionResponseDTO;
import idatt2105.peakquizbackend.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

  QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

  @Mapping(target = "id", ignore = true)
  Question fromQuestionCreateDTOtoEntity(QuestionCreateDTO questionCreateDTO);

  Question fromQuestionResponseDTOtoEntity(QuestionResponseDTO questionResponseDTO);
  @Mapping(target = "id", ignore = true)
  void updateQuestionFromDTO(QuestionResponseDTO questionResponseDTO, @MappingTarget Question question);
  QuestionResponseDTO toDTO(Question question);
}
