package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface ProductService {

    GlobalResponse addProduct(ProductDto productDto, MultipartFile multipartFile);
    GlobalResponse getAllProducts(String search, List<Long> categoryId);
    Resource getProductImageById(Long productId) throws MalformedURLException;
    GlobalResponse getAllProductById(Long id);
    GlobalResponse updateProduct(ProductDto productDto, MultipartFile multipartFile);
    GlobalResponse deleteProduct(Long id);
}
