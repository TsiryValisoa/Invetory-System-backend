package mg.tsiry.invetory_management_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import mg.tsiry.invetory_management_system.data.entities.Transaction;
import mg.tsiry.invetory_management_system.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;
}
