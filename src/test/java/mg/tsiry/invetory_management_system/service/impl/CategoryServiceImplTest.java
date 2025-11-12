package mg.tsiry.invetory_management_system.service.impl;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Category;
import mg.tsiry.invetory_management_system.data.repositories.CategoryRepository;
import mg.tsiry.invetory_management_system.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldCreateCategory() {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Electronics");

        Category category = new Category();
        category.setName("Electronics");

        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        GlobalResponse globalResponse = categoryService.createCategory(categoryDto);

        assertNotNull(globalResponse);
        assertEquals(200, globalResponse.getStatus());
    }
}