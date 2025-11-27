package mg.tsiry.invetory_management_system.data.repositories;

import mg.tsiry.invetory_management_system.data.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            "SELECT p FROM Product p " +
                    "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))" +
                    " OR LOWER(p.description) LIKE CONCAT('%', LOWER(:search), '%')"
    )
    Page<Product> findProductByNameOrDescription(@Param("search") String search, Pageable pageable);

    Page<Product> findByCategoryIdIn(List<Long> categoryId, Pageable pageable);
}
