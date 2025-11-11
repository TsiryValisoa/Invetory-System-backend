package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.UserDto;

public interface UserService {

    GlobalResponse addUser(UserDto userDto);
    GlobalResponse loginUser(UserDto userDto);
    GlobalResponse getAllUsers();
    UserDto getCurrentLoggedUser();
    GlobalResponse updateUser(Long id, UserDto userDto);
    GlobalResponse deleteUser(Long id);
    GlobalResponse getUserTransaction(Long id);

}
