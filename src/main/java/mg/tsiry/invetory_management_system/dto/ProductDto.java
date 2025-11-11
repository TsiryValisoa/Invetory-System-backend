package mg.tsiry.invetory_management_system.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;
    private Long productId;
    private Long categoryId;
    private Long supplierId;
    private String name;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private String description;
    private String imageUrl;
    private LocalDate expiryDate;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
