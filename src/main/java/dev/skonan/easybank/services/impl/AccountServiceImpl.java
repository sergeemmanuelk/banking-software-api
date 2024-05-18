package dev.skonan.easybank.services.impl;

import dev.skonan.easybank.dto.AccountDto;
import dev.skonan.easybank.exceptions.OperationNonPermittedException;
import dev.skonan.easybank.models.Account;
import dev.skonan.easybank.repositories.AccountRepository;
import dev.skonan.easybank.services.AccountService;
import dev.skonan.easybank.validators.ObjectsValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ObjectsValidator<AccountDto> validator;

    @Override
    public Integer save(AccountDto dto) {

        // Block account update -> iban cannot be changed

        /*if (dto.getId() != null) {
            throw new OperationNonPermittedException(
                "Account cannot be updated",
                "Save Account",
                "Account",
                "Update not permitted"
            );
        }*/

        validator.validate(dto);

        Account account = AccountDto.toEntity(dto);

        boolean userHasAlreadyAnAccount = accountRepository.findByUserId(account.getUser().getId()).isPresent();

        if (userHasAlreadyAnAccount && account.getUser().isActive()) {
            throw new OperationNonPermittedException(
                "The selected user has already an active account",
                "Create account",
                "Account service",
                "Account creation"
            );
        }

        if (dto.getId() == null) {
            account.setIban(generateRandomIban());
        }

        return accountRepository.save(account).getId();
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto findById(Integer id) {
        return accountRepository.findById(id)
                .map(AccountDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No account was found with the provided ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

    private String generateRandomIban() {
        // Generate Iban
        String iban = Iban.random(CountryCode.CI).toFormattedString();

        // Check if the iban already exists
        boolean ibanExists = accountRepository.findByIban(iban).isPresent();

        // If exists -> generate new random iban
        if (ibanExists) {
            generateRandomIban();
        }

        // Else -> return generated iban
        return iban;
    }
}
