package idatt2105.peakquizbackend.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for updating user information.
 */
@Data
public class UserUpdateDTO {

    /**
     * The new password to be updated.
     */
    private String password;

}
