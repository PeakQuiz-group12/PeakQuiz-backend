package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.QuizCreateDTO;
import idatt2105.peakquizbackend.dto.QuizResponseDTO;
import idatt2105.peakquizbackend.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
@Mapper
public interface QuizMapper {

    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "id", ignore = true)
    Quiz fromQuizCreateDTOtoEntity(QuizCreateDTO quizCreateDTO);


    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "collaborators", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateQuizFromDTO(QuizResponseDTO quizResponseDTO, @MappingTarget Quiz quiz);
    QuizResponseDTO toDTO(Quiz quiz);

}
