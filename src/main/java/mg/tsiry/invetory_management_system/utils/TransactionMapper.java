package mg.tsiry.invetory_management_system.utils;

import mg.tsiry.invetory_management_system.controller.request.TransactionRequest;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import mg.tsiry.invetory_management_system.dto.SupplierDto;
import mg.tsiry.invetory_management_system.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toTransactionDto(TransactionRequest transactionRequest) {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setQuantity(transactionRequest.getQuantity());
        transactionDto.setDescription(transactionRequest.getDescription());

        ProductDto productDto = new ProductDto();
        productDto.setId(transactionRequest.getProductId());
        transactionDto.setProduct(productDto);

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(transactionRequest.getSupplierId());
        transactionDto.setSupplier(supplierDto);

        return transactionDto;
    }

}
