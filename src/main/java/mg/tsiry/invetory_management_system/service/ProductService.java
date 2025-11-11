package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    GlobalResponse addProduct(ProductDto productDto, MultipartFile multipartFile);
    GlobalResponse getAllProducts();
    GlobalResponse getAllProductById(Long id);
    GlobalResponse updateProduct(ProductDto productDto, MultipartFile multipartFile);
    GlobalResponse deleteProduct(Long id);
}
