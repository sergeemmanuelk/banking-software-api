package dev.skonan.easybank.dto;

import dev.skonan.easybank.models.Transaction;
import dev.skonan.easybank.models.TransactionType;
import dev.skonan.easybank.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Integer id;
    private BigDecimal amount;
    private TransactionType type;
    private String destinationIban;
    private LocalDate transactionDate;
    private Integer userId;

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .destinationIban(transaction.getDestinationIban())
                .transactionDate(transaction.getTransactionDate())
                .userId(transaction.getUser().getId())
                .build();
    }

    public static Transaction toEntity(TransactionDto transaction) {
        return Transaction.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .destinationIban(transaction.getDestinationIban())
                .transactionDate(LocalDate.now())
                .user(
                    User.builder()
                        .id(transaction.getUserId())
                        .build()
                )
                .build();
    }
}
