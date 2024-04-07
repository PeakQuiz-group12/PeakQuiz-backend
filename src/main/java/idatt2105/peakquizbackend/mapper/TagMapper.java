package idatt2105.peakquizbackend.mapper;

import com.sun.mail.util.QEncoderStream;
import idatt2105.peakquizbackend.dto.QuestionDTO;
import idatt2105.peakquizbackend.dto.TagDTO;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper interface for converting between Tag entities and DTOs.
 */
@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class TagMapper {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;

    /**
     * INSTANCE variable for accessing the mapper instance.
     */
    public static final TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    /**
     * Converts a TagDTO to a Tag entity.
     *
     * @param tagDTO
     *            The TagDTO to convert
     * @return The corresponding Tag entity
     */
    @Mapping(target = "user", source = "username", qualifiedByName = "mapUser")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "mapQuestions")
    @Mapping(target = "id", source = "id")
    public abstract Tag fromTagDTOtoEntity(TagDTO tagDTO);

    /**
     * Converts a Tag entity to a TagDTO.
     *
     * @param tag
     *            The Tag entity to convert
     * @return The corresponding TagDTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "username", source = "user", qualifiedByName = "mapToUsername")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "mapToQuestionDTOs")
    public abstract TagDTO toDTO(Tag tag);

    /**
     * Updates a Tag entity from a TagDTO.
     *
     * @param tagDTO
     *            The TagDTO to update from
     * @param tag
     *            The target Tag entity to update
     */
    @Mapping(target = "user", source = "username", qualifiedByName = "mapUser")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "questions", source = "questions", qualifiedByName = "mapQuestions")
    @Mapping(target = "id", source = "id")
    public abstract void updateTagFromDTO(TagDTO tagDTO, @MappingTarget Tag tag);

    /**
     * Maps a set of Question entities to a set of QuestionDTOs.
     *
     * @param questions
     *            The set of Question entities to map
     * @return The set of corresponding QuestionDTOs
     */
    @Named("mapToQuestionDTOs")
    public Set<QuestionDTO> mapToQuestionDTOs(Set<Question> questions) {
        return questions.stream().map(QuestionMapper.INSTANCE::toDTO).collect(Collectors.toSet());
    }

    /**
     * Maps a User entity to its username.
     *
     * @param user
     *            The User entity to map
     * @return The corresponding username
     */
    @Named("mapToUsername")
    public String mapToUsername(User user) {
        return user.getUsername();
    }

    /**
     * Maps a username to a User entity.
     *
     * @param username
     *            The username to map
     * @return The corresponding User entity
     */
    @Named("mapUser")
    public User mapUser(String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * Maps a set of QuestionDTOs to a set of Question entities.
     *
     * @param questions
     *            The set of QuestionDTOs to map
     * @return The set of corresponding Question entities
     */
    @Named("mapQuestions")
    public Set<Question> mapQuestions(Set<QuestionDTO> questions) {
        return questions.stream().map(questionMapper::fromQuestionDTOtoEntity).collect(Collectors.toSet());
    }
}
