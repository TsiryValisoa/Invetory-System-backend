package mg.tsiry.invetory_management_system.controller.request;

import lombok.Data;

@Data
public class TransactionRequest {

    private Long productId;
    private Integer quantity;
    private Long supplierId;
    private String description;
}
