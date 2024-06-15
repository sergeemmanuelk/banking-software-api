package dev.skonan.easybank.services;

import dev.skonan.easybank.dto.TransactionDto;

import java.util.List;

public interface TransactionService extends AbstractService<TransactionDto> {
    List<TransactionDto> findAllByUserId(Integer userId);
}
