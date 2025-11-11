package mg.tsiry.invetory_management_system.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required!")
    private String name;
    @NotBlank(message = "Supplier address is required!")
    private String address;
}
