package dev.skonan.easybank.services.impl;

import dev.skonan.easybank.config.JwtUtils;
import dev.skonan.easybank.dto.AccountDto;
import dev.skonan.easybank.dto.AuthenticationRequest;
import dev.skonan.easybank.dto.AuthenticationResponse;
import dev.skonan.easybank.dto.UserDto;
import dev.skonan.easybank.models.User;
import dev.skonan.easybank.repositories.UserRepository;
import dev.skonan.easybank.services.AccountService;
import dev.skonan.easybank.services.UserService;
import dev.skonan.easybank.validators.ObjectsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ObjectsValidator<UserDto> validator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public Integer save(UserDto dto) {
        validator.validate(dto);

        //Transform userDto to user object
        User user = UserDto.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    @Transactional
    public Integer validateAccount(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No user was found for user account validation"));

        // Create bank account
        AccountDto account = AccountDto.builder()
                .user(UserDto.fromEntity(user))
                .build();

        accountService.save(account);

        user.setActive(true);
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

    @Override
    public AuthenticationResponse register(UserDto userDto) {
        validator.validate(userDto);

        User user = UserDto.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("fullName", savedUser.getFirstName() + " " + savedUser.getLastName());
        String token = jwtUtils.generateToken(savedUser, claims);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        final User user = userRepository.findByEmail(authenticationRequest.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullName", user.getFirstName() + " " + user.getLastName());
        final String token = jwtUtils.generateToken(user, claims);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
