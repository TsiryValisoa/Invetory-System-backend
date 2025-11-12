package mg.tsiry.invetory_management_system.service.impl;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Product;
import mg.tsiry.invetory_management_system.data.entities.Supplier;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.ProductRepository;
import mg.tsiry.invetory_management_system.data.repositories.SupplierRepository;
import mg.tsiry.invetory_management_system.data.repositories.TransactionRepository;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import mg.tsiry.invetory_management_system.dto.SupplierDto;
import mg.tsiry.invetory_management_system.dto.TransactionDto;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldRestockInventory() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setStockQuantity(5);

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Supplier A");

        User user = new User();
        user.setId(1L);
        user.setName("Admin");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(1L);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setProduct(productDto);
        transactionDto.setSupplier(supplierDto);
        transactionDto.setQuantity(3);
        transactionDto.setDescription("Restock laptop");

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(userService.getCurrentLoggedUser()).thenReturn(userDto);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);

        GlobalResponse response = transactionService.restockInventory(transactionDto);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(8, product.getStockQuantity());
    }
}