package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.CategoryRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.CategoryDto;
import mg.tsiry.invetory_management_system.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryDto categoryDto = modelMapper.map(categoryRequest, CategoryDto.class);
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> listAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> listCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> updateCategory(@PathVariable Long id,
                                                         @RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryDto categoryDto = modelMapper.map(categoryRequest, CategoryDto.class);
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GlobalResponse> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
