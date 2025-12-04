package mg.tsiry.invetory_management_system.config;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.UserRepository;
import mg.tsiry.invetory_management_system.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createAdminIfNotExists(String email, String password) {

        if (!userRepository.existsByEmail(email)) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }
    }
}
