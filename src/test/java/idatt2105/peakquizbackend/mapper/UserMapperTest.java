package idatt2105.peakquizbackend.mapper;

import idatt2105.peakquizbackend.dto.UserDTO;
import idatt2105.peakquizbackend.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserMapperTest {

    @Test
    public void testUserToUserDTO() {
        User user = new User("name", "email@mail.com", "password");
        user.setId(1L);

        UserDTO dto = UserMapper.INSTANCE.toDTO(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUsername(), dto.getUsername());
    }
}
