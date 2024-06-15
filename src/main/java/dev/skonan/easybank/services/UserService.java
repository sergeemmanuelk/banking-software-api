package dev.skonan.easybank.services;

import dev.skonan.easybank.dto.AuthenticationRequest;
import dev.skonan.easybank.dto.AuthenticationResponse;
import dev.skonan.easybank.dto.UserDto;

public interface UserService extends AbstractService<UserDto> {
    Integer validateAccount(Integer id);

    Integer invalidateAccount(Integer id);

    AuthenticationResponse register(UserDto user);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
