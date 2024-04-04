package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuestionCreateDTO;
import idatt2105.peakquizbackend.dto.QuestionResponseDTO;
import idatt2105.peakquizbackend.model.Question;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

  QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);


  @Mapping(target = "quiz",  ignore = true)
  @Mapping(target = "id", ignore = true)
  Question fromQuestionCreateDTOtoEntity(QuestionCreateDTO questionCreateDTO);

  @Mapping(target = "quiz", ignore = true)
  Question fromQuestionResponseDTOtoEntity(QuestionResponseDTO questionResponseDTO);
  @Mapping(target = "quiz", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateQuestionFromDTO(QuestionResponseDTO questionResponseDTO, @MappingTarget Question question);
  QuestionResponseDTO toDTO(Question question);
}
