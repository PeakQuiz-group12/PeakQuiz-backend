package idatt2105.peakquizbackend.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for user information.
 */
@Data
public class UserDTO {

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The email address of the user.
     */
    private String email;
}
