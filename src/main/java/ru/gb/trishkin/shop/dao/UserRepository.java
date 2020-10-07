package ru.gb.trishkin.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.trishkin.shop.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}
