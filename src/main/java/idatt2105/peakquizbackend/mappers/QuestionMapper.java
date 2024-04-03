package idatt2105.peakquizbackend.mappers;

import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

  QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

  @Mapping(target = "id", ignore = true)
  Question toEntity(QuestionDTO questionDTO);
  @Mapping(target = "id", ignore = true)
  void updateQuestionFromDTO(QuestionDTO questionDTO, @MappingTarget Question question);
  QuestionDTO toDTO(Question question);
}
