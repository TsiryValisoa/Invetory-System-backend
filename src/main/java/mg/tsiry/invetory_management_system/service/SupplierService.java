package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.SupplierDto;

public interface SupplierService {

    GlobalResponse addSupplier(SupplierDto supplierDto);
    GlobalResponse getAllSupplier();
    GlobalResponse updateSupplier(Long id, SupplierDto supplierDto);
    GlobalResponse getSupplierById(Long id);
    GlobalResponse deleteSupplier(Long id);
}
