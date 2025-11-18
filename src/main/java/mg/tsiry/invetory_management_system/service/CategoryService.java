package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.CategoryDto;

public interface CategoryService {

    GlobalResponse createCategory(CategoryDto categoryDto);
    GlobalResponse getAllCategories(String search);
    GlobalResponse getCategoryById(Long id);
    GlobalResponse updateCategory(Long id, CategoryDto categoryDto);
    GlobalResponse deleteCategory(Long id);

}
