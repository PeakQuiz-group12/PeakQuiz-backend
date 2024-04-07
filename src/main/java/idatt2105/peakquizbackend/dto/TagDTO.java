package idatt2105.peakquizbackend.dto;

import lombok.Data;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for tag information.
 */
@Data
public class TagDTO {

    /**
     * The unique identifier of the tag.
     */
    private Long id;

    /**
     * The title of the tag.
     */
    private String title;

    /**
     * The username associated with the tag.
     */
    private String username;

    /**
     * Set of questions associated with the tag.
     */
    private Set<QuestionDTO> questions;
}
