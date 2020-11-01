package ru.gb.trishkin.shop.service;

import org.springframework.stereotype.Service;
import ru.gb.trishkin.shop.dao.OrderRepository;
import ru.gb.trishkin.shop.domain.Order;

import javax.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
