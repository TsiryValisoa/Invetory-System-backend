package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.LoginRequest;
import mg.tsiry.invetory_management_system.controller.request.RegisterRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<GlobalResponse> addUser(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDto userDto = modelMapper.map(registerRequest, UserDto.class);
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        UserDto userDto = modelMapper.map(loginRequest, UserDto.class);
        return ResponseEntity.ok(userService.loginUser(userDto));
    }

}
