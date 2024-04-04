package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    @Mapping(target = "id", ignore = true)
    Quiz fromQuizCreateDTOtoEntity(QuizCreateDTO quizCreateDTO);
    @Mapping(target = "id", ignore = true)
    void updateQuizFromDTO(QuizResponseDTO quizResponseDTO, @MappingTarget Quiz quiz);
    QuizResponseDTO toDTO(Quiz quiz);

}
