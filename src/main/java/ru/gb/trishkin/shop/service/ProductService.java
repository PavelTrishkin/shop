package ru.gb.trishkin.shop.service;

import ru.gb.trishkin.shop.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll();

    void addToUserBucket(Long productId, String username);
}
