package dev.skonan.easybank.services.impl;

import dev.skonan.easybank.dto.TransactionSumDetails;
import dev.skonan.easybank.models.TransactionType;
import dev.skonan.easybank.repositories.TransactionRepository;
import dev.skonan.easybank.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionSumDetails> findSumTransactionsByDate(LocalDate startDate, LocalDate endDate, Integer userId) {

        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));

        return transactionRepository.findSumTransactionsByDate(start, end, userId);
    }

    @Override
    public BigDecimal getAccountBalancer(Integer userId) {
        return transactionRepository.findAccountBalance(userId);
    }

    @Override
    public BigDecimal highestTransfer(Integer userId) {
        return transactionRepository.findHighestAmountByTransactionType(userId, TransactionType.TRANSFER);
    }

    @Override
    public BigDecimal highestDeposit(Integer userId) {
        return transactionRepository.findHighestAmountByTransactionType(userId, TransactionType.DEPOSIT);
    }
}
