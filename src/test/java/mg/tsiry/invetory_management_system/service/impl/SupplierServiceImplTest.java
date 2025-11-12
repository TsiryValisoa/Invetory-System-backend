package mg.tsiry.invetory_management_system.service.impl;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Supplier;
import mg.tsiry.invetory_management_system.data.repositories.SupplierRepository;
import mg.tsiry.invetory_management_system.dto.SupplierDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void shouldAddSupplier() {

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setName("EVL");
        supplierDto.setAddress("Antananarivo II B");

        Supplier supplier = new Supplier();
        supplier.setName("EVL");
        supplier.setAddress("Antananarivo II B");

        when(modelMapper.map(supplierDto, Supplier.class)).thenReturn(supplier);
        when(supplierRepository.save(supplier)).thenReturn(supplier);

        GlobalResponse globalResponse = supplierService.addSupplier(supplierDto);

        assertNotNull(globalResponse);
        assertEquals(200, globalResponse.getStatus());
    }

}