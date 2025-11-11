package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.TransactionDto;
import mg.tsiry.invetory_management_system.enums.TransactionStatus;

public interface TransactionService {

    GlobalResponse restockInventory(TransactionDto transactionDto);
    GlobalResponse sell(TransactionDto transactionDto);
    GlobalResponse returnToSupplier(TransactionDto transactionDto);
    GlobalResponse getAllTransactions(int page, int size, String searchText);
    GlobalResponse getTransactionById(Long id);
    GlobalResponse getTransactionByMonthAndYear(int month, int year);
    GlobalResponse updateTransactionStatus(Long id, TransactionStatus transactionStatus);
}
