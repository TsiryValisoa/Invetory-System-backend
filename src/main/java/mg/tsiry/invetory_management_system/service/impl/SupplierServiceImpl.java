package mg.tsiry.invetory_management_system.service.impl;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Supplier;
import mg.tsiry.invetory_management_system.data.repositories.SupplierRepository;
import mg.tsiry.invetory_management_system.dto.SupplierDto;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public GlobalResponse addSupplier(SupplierDto supplierDto) {

        Supplier supplierToSave = modelMapper.map(supplierDto, Supplier.class);
        supplierRepository.save(supplierToSave);

        return GlobalResponse.builder()
                .status(200)
                .message("Supplier added successfully.")
                .build();
    }

    @Override
    public GlobalResponse getAllSupplier() {

        List<Supplier> supplierList = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<SupplierDto> supplierDtoList = modelMapper.map(supplierList, new TypeToken<List<SupplierDto>>() {}.getType());

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .suppliers(supplierDtoList)
                .build();
    }

    @Override
    public GlobalResponse updateSupplier(Long id, SupplierDto supplierDto) {

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found!"));

        existingSupplier.setName(supplierDto.getName());
        existingSupplier.setAddress(supplierDto.getAddress());

        supplierRepository.save(existingSupplier);

        return GlobalResponse.builder()
                .status(200)
                .message("Supplier successfully updated.")
                .build();
    }

    @Override
    public GlobalResponse getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found!"));

        SupplierDto supplierDto = modelMapper.map(supplier, SupplierDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public GlobalResponse deleteSupplier(Long id) {

        supplierRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Supplier not found!"));

        supplierRepository.deleteById(id);

        return GlobalResponse.builder()
                .status(200)
                .message("Supplier successfully deleted.")
                .build();
    }
}
