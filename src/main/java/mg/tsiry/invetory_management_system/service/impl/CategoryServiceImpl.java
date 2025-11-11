package mg.tsiry.invetory_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Category;
import mg.tsiry.invetory_management_system.data.repositories.CategoryRepository;
import mg.tsiry.invetory_management_system.dto.CategoryDto;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public GlobalResponse createCategory(CategoryDto categoryDto) {

        Category categoryToSave = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(categoryToSave);

        return GlobalResponse.builder()
                .status(200)
                .message("Category created successfully.")
                .build();
    }

    @Override
    public GlobalResponse getAllCategories() {

        List<Category> categoryList = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<CategoryDto> categoryDtoList = modelMapper.map(categoryList, new TypeToken<List<CategoryDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .categories(categoryDtoList)
                .build();

    }

    @Override
    public GlobalResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .category(categoryDto)
                .build();
    }

    @Override
    public GlobalResponse updateCategory(Long id, CategoryDto categoryDto) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        existingCategory.setName(categoryDto.getName());
        categoryRepository.save(existingCategory);

        return GlobalResponse.builder()
                .status(200)
                .message("Category successfully updated.")
                .build();
    }

    @Override
    public GlobalResponse deleteCategory(Long id) {

        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        categoryRepository.deleteById(id);

        return GlobalResponse.builder()
                .status(200)
                .message("Category successfully deleted.")
                .build();
    }
}
