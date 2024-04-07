package idatt2105.peakquizbackend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TagDTO {

    private Long id;
    private String title;
    private String username;
    private Set<QuestionDTO> questions;

}
