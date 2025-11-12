package mg.tsiry.invetory_management_system.service.impl;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.repositories.UserRepository;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.enums.UserRole;
import mg.tsiry.invetory_management_system.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldAddUser() {

        UserDto userDto = new UserDto();
        userDto.setName("Alice");
        userDto.setEmail("alice@example.com");
        userDto.setPassword("password123");
        userDto.setPhoneNumber("123456789");
        userDto.setRole(null);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        GlobalResponse response = userService.addUser(userDto);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        verify(userRepository, times(1)).save(argThat(user ->
                user.getName().equals("Alice") &&
                        user.getEmail().equals("alice@example.com") &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getPhoneNumber().equals("123456789") &&
                        user.getRole() == UserRole.MANAGER
        ));
        verify(passwordEncoder, times(1)).encode("password123");
    }
}