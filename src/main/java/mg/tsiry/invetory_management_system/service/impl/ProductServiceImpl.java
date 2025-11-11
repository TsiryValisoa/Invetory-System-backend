package mg.tsiry.invetory_management_system.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Category;
import mg.tsiry.invetory_management_system.data.entities.Product;
import mg.tsiry.invetory_management_system.data.repositories.CategoryRepository;
import mg.tsiry.invetory_management_system.data.repositories.ProductRepository;
import mg.tsiry.invetory_management_system.dto.ProductDto;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.service.ProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image";

    @Override
    public GlobalResponse addProduct(ProductDto productDto, MultipartFile multipartFile) {

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found!"));

        Product productToSave = Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();

        if (multipartFile != null) {
            String imagePath = saveImage(multipartFile);
            productToSave.setImageUrl(imagePath);
        }

        productRepository.save(productToSave);

        return GlobalResponse.builder()
                .status(200)
                .message("Product successfully saved.")
                .build();
    }

    @Override
    public GlobalResponse getAllProducts() {

        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDto> productDtoList = modelMapper.map(productList, new TypeToken<List<ProductDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .products(productDtoList)
                .build();
    }

    @Override
    public GlobalResponse getAllProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));

        ProductDto productDto = modelMapper.map(product, ProductDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .product(productDto)
                .build();
    }

    @Override
    public GlobalResponse updateProduct(ProductDto productDto, MultipartFile multipartFile) {

        Product existingProduct = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found!"));

        //Check if image is associated with the update request
        if (multipartFile != null && !multipartFile.isEmpty()) {
            //Delete old image if exist
            if (existingProduct.getImageUrl() != null) {
                File oldFile = new File(existingProduct.getImageUrl());
                oldFile.delete();
            }
            String imagePath = saveImage(multipartFile);
            existingProduct.setImageUrl(imagePath);
        }

        //Check if the category is to be changed for the product
        if (productDto.getCategoryId() != null && productDto.getCategoryId() > 0) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found!"));
            existingProduct.setCategory(category);
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setSku(productDto.getSku());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setExpiryDate(productDto.getExpiryDate());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        productRepository.save(existingProduct);

        return GlobalResponse.builder()
                .status(200)
                .message("Product successfully updated.")
                .build();
    }

    @Override
    public GlobalResponse deleteProduct(Long id) {

        productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Category not found!"));

        productRepository.deleteById(id);

        return GlobalResponse.builder()
                .status(200)
                .message("Product successfully deleted.")
                .build();
    }

    private String saveImage(MultipartFile imageFile) {

        //Validate image check
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed!");
        }

        //Create the directory to store images if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            log.info("Directory was created");
        }

        //Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        //Get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + File.separator + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error occurred while saving image" + e.getMessage());
        }

        return imagePath;
    }
}
