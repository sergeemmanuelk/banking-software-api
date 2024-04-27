package dev.skonan.easybank.services.impl;

import dev.skonan.easybank.dto.AccountDto;
import dev.skonan.easybank.dto.UserDto;
import dev.skonan.easybank.models.User;
import dev.skonan.easybank.repositories.UserRepository;
import dev.skonan.easybank.services.AccountService;
import dev.skonan.easybank.services.UserService;
import dev.skonan.easybank.validators.ObjectsValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ObjectsValidator<UserDto> validator;

    @Override
    public Integer save(UserDto dto) {
        validator.validate(dto);

        //Transform userDto to user object
        User user = UserDto.toEntity(dto);

        return userRepository.save(user).getId();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer id) {
        return userRepository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No user was found with the provided ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Integer validateAccount(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        user.setActive(true);

        // Create bank account
        AccountDto account = AccountDto.builder()
                .user(UserDto.fromEntity(user))
                .build();

        accountService.save(account);
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public Integer invalidateAccount(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        user.setActive(false);
        userRepository.save(user);

        return user.getId();
    }
}
