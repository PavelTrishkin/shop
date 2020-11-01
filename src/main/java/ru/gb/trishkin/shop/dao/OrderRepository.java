package ru.gb.trishkin.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.trishkin.shop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
