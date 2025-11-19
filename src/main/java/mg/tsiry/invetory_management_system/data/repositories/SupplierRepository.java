package mg.tsiry.invetory_management_system.data.repositories;

import mg.tsiry.invetory_management_system.data.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(
            "SELECT s FROM Supplier s " +
                    "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))" +
                    " OR LOWER(s.address) LIKE CONCAT('%', LOWER(:search), '%')"
    )
    Page<Supplier> findByNameOrAddress(@Param("search") String search, Pageable pageable);
}
