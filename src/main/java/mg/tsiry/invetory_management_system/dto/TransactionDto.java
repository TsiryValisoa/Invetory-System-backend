package mg.tsiry.invetory_management_system.dto;

import lombok.Data;
import mg.tsiry.invetory_management_system.enums.TransactionStatus;
import mg.tsiry.invetory_management_system.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long id;
    private Integer quantity;
    private Integer totalProducts;
    private BigDecimal totalPrice;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private UserDto user;
    private ProductDto product;
    private SupplierDto supplier;
}
