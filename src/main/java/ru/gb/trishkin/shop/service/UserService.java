package ru.gb.trishkin.shop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gb.trishkin.shop.dto.UserDto;

public interface UserService extends UserDetailsService {
    boolean save(UserDto userDto);
}
