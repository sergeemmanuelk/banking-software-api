package dev.skonan.easybank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractEntity {
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "destination_iban")
    private String destinationIban;

    @Column(name = "transaction_date", updatable = false)
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
