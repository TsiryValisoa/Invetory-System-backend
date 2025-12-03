package mg.tsiry.invetory_management_system.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * A class that handle all the operation on products.
 *
 * @author Tsiry Valisoa
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Value("${IMAGE_DIRECTORY}")
    private String baseImage;
    private static final String BASE_DIRECTORY = "IMS-product-image";

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
    public GlobalResponse getAllProducts(int page, int size, String search, List<Long> categoryId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productRepository
                    .findProductByNameOrDescription(search, pageable);
        } else if (categoryId != null && !categoryId.isEmpty()) {
            productPage = productRepository
                    .findByCategoryIdIn(categoryId, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductDto> productDtoList = modelMapper.map(productPage.getContent(), new TypeToken<List<ProductDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .currentPage(page)
                .totalElement(productPage.getTotalElements())
                .totalPage(productPage.getTotalPages())
                .products(productDtoList)
                .build();
    }

    @Override
    public Resource getProductImageById(Long productId) throws MalformedURLException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found !"));

        String filename = product.getImageUrl();

        if (filename == null || filename.isEmpty()) {
            throw new NotFoundException("Product has no associated image.");
        }

        Path imagePath = Paths.get(baseImage, "IMS-product-image", filename);
        Resource resource = new UrlResource(imagePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new NotFoundException("Product image not found !");
        }

        return resource;
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
            if (existingProduct.getImageUrl() != null &&
                    !existingProduct.getImageUrl().isEmpty()) {
                File oldFile = new File(baseImage +
                        File.separator +
                        BASE_DIRECTORY +
                        File.separator +
                        existingProduct.getImageUrl());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
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
    @Transactional
    public GlobalResponse deleteProduct(Long id) {

        Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found!"));

        if (product.getImageUrl() != null &&
                !product.getImageUrl().isEmpty()) {
            File oldFile = new File(baseImage +
                    File.separator +
                    BASE_DIRECTORY +
                    File.separator +
                    product.getImageUrl());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        productRepository.delete(product);

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
        File directory = new File(baseImage + File.separator + BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
            log.info("Directory was created");
        }

        //Generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        //Get the absolute path of the image
        String imagePath = directory.getAbsolutePath() + File.separator + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error occurred while saving image" + e.getMessage());
        }

        return uniqueFileName;
    }
}
