package mg.tsiry.invetory_management_system.controller;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import mg.tsiry.invetory_management_system.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> addProduct(@RequestParam("imageFile") MultipartFile imageFile,
                                                     @RequestParam("name") String name,
                                                     @RequestParam("sku") String sku,
                                                     @RequestParam("price") BigDecimal price,
                                                     @RequestParam("stockQuantity") Integer stockQuantity,
                                                     @RequestParam("categoryId") Long categoryId,
                                                     @RequestParam(value = "description", required = false) String description
    ) {

        ProductDto productDto = new ProductDto();

        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setDescription(description);

        return ResponseEntity.ok(productService.addProduct(productDto, imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> listAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> listProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getAllProductById(id));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "expiryDate", required = false) LocalDate expiryDate
    ) {

        ProductDto productDto = new ProductDto();

        productDto.setProductId(id);
        productDto.setName(name);
        productDto.setSku(sku);
        productDto.setPrice(price);
        productDto.setStockQuantity(stockQuantity);
        productDto.setCategoryId(categoryId);
        productDto.setDescription(description);
        productDto.setExpiryDate(expiryDate);

        return ResponseEntity.ok(productService.updateProduct(productDto, imageFile));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

}
