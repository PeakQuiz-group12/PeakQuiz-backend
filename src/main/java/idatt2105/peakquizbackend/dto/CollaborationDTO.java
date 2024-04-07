package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a collaboration.
 */
@Data
public class CollaborationDTO {

    /**
     * The ID of the user involved in the collaboration.
     */
    private Long userId;

    /**
     * The ID of the quiz involved in the collaboration.
     */
    private Long quizId;

    /**
     * The type of collaboration.
     */
    private CollaboratorType collaboratorType;
}
