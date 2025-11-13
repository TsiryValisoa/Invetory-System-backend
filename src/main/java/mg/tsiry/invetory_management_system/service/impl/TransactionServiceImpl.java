package mg.tsiry.invetory_management_system.service.impl;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.Product;
import mg.tsiry.invetory_management_system.data.entities.Supplier;
import mg.tsiry.invetory_management_system.data.entities.Transaction;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.ProductRepository;
import mg.tsiry.invetory_management_system.data.repositories.SupplierRepository;
import mg.tsiry.invetory_management_system.data.repositories.TransactionRepository;
import mg.tsiry.invetory_management_system.dto.TransactionDto;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.enums.TransactionStatus;
import mg.tsiry.invetory_management_system.enums.TransactionType;
import mg.tsiry.invetory_management_system.exception.NameValueRequiredException;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.service.TransactionService;
import mg.tsiry.invetory_management_system.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A class that handle all inventory-transaction operations.
 *
 * @author Tsiry Valisoa
 */
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserService userService;

    @Override
    public GlobalResponse restockInventory(TransactionDto transactionDto) {

        Product product = getProduct(transactionDto.getProduct().getId());
        Supplier supplier = getSupplier(transactionDto.getSupplier().getId());
        Integer quantity = transactionDto.getQuantity();
        User user = getCurrentUser();

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //Save transaction
        saveTransaction(
                product,
                user,
                supplier,
                quantity,
                TransactionType.PURCHASE,
                TransactionStatus.COMPLETED,
                null,
                transactionDto.getDescription()
        );

        return GlobalResponse.builder()
                .status(200)
                .message("Transaction made successfully.")
                .build();
    }

    @Override
    public GlobalResponse sell(TransactionDto transactionDto) {

        Product product = getProduct(transactionDto.getProduct().getId());
        Integer quantity = transactionDto.getQuantity();
        User user = getCurrentUser();

        //Update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //Save transaction
        saveTransaction(
                product,
                user,
                null,
                quantity,
                TransactionType.SALE,
                TransactionStatus.COMPLETED,
                null,
                transactionDto.getDescription()
        );

        return GlobalResponse.builder()
                .status(200)
                .message("Transaction sold successfully.")
                .build();
    }

    @Override
    public GlobalResponse returnToSupplier(TransactionDto transactionDto) {

        Product product = getProduct(transactionDto.getProduct().getId());
        Supplier supplier = getSupplier(transactionDto.getSupplier().getId());
        Integer quantity = transactionDto.getQuantity();
        User user = getCurrentUser();

        //Update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //Save transaction
        saveTransaction(
                product,
                user,
                supplier,
                quantity,
                TransactionType.RETURN_TO_SUPPLIER,
                TransactionStatus.PROCESSING,
                BigDecimal.ZERO,
                transactionDto.getDescription()
        );

        return GlobalResponse.builder()
                .status(200)
                .message("Transaction return successfully initialized.")
                .build();
    }

    @Override
    public GlobalResponse getAllTransactions(int page, int size, String searchText) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable);

        List<TransactionDto> transactionDtoList = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDtoList.forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setProduct(null);
            transactionDto.setSupplier(null);
        });

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .transactions(transactionDtoList)
                .build();
    }

    @Override
    public GlobalResponse getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));

        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .transaction(transactionDto)
                .build();
    }

    @Override
    public GlobalResponse getTransactionByMonthAndYear(int month, int year) {

        List<Transaction> transactionList = transactionRepository.findAllByMonthAndYear(month, year);

        List<TransactionDto> transactionDtoList = modelMapper
                .map(transactionList, new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDtoList.forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setProduct(null);
            transactionDto.setSupplier(null);
        });

        return GlobalResponse.builder()
                .status(200)
                .message("Success.")
                .transactions(transactionDtoList)
                .build();
    }

    @Override
    public GlobalResponse updateTransactionStatus(Long id, TransactionStatus transactionStatus) {

        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found!"));

        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return GlobalResponse.builder()
                .status(200)
                .message("Transaction successfully updated.")
                .build();
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
    }

    private Supplier getSupplier(Long supplierId) {
        if (supplierId == null) {
            throw new NameValueRequiredException("Supplier ID is required!");
        }
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found!"));
    }

    private User getCurrentUser() {
        UserDto userDto = userService.getCurrentLoggedUser();
        return modelMapper.map(userDto, User.class);
    }

    /**
     * Generic method to create and save transaction
     */
    private void saveTransaction(
            Product product,
            User user,
            Supplier supplier,
            Integer quantity,
            TransactionType type,
            TransactionStatus status,
            BigDecimal totalPrice,
            String description
    ) {

        Transaction transaction = Transaction.builder()
                .transactionType(type)
                .status(status)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(totalPrice != null ? totalPrice : product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(description)
                .build();

        transactionRepository.save(transaction);
    }
}
