package mg.tsiry.invetory_management_system.service.impl;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Category;
import mg.tsiry.invetory_management_system.data.repositories.CategoryRepository;
import mg.tsiry.invetory_management_system.data.repositories.ProductRepository;
import mg.tsiry.invetory_management_system.dto.ProductDto;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldAddProduct() {

        ProductDto productDto = new ProductDto();
        productDto.setName("Laptop");
        productDto.setSku("SKU123");
        productDto.setPrice(BigDecimal.valueOf(1500.0));
        productDto.setStockQuantity(10);
        productDto.setDescription("High-end laptop");
        productDto.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        GlobalResponse globalResponse = productService.addProduct(productDto, null);

        assertNotNull(globalResponse);
        assertEquals(200, globalResponse.getStatus());
    }

}