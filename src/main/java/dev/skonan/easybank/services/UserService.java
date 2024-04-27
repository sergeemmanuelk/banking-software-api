package dev.skonan.easybank.services;

import dev.skonan.easybank.dto.UserDto;

public interface UserService extends AbstractService<UserDto> {
    Integer validateAccount(Integer id);

    Integer invalidateAccount(Integer id);


}
