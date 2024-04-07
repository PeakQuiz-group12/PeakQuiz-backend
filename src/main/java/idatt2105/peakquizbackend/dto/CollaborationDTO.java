package idatt2105.peakquizbackend.dto;

import idatt2105.peakquizbackend.model.enums.CollaboratorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CollaborationDTO {

    private Long userId;
    private Long quizId;
    private CollaboratorType collaboratorType;
}
