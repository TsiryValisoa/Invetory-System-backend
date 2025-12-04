package mg.tsiry.invetory_management_system.data.repositories;

import mg.tsiry.invetory_management_system.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))" +
            " OR LOWER(CONCAT('', u.role)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> findByNameOrRole(String search, Pageable pageable);
}
