package ru.gb.trishkin.shop.service;

import ru.gb.trishkin.shop.domain.Product;
import ru.gb.trishkin.shop.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll();

    void addToUserBucket(Long productId, String username);

    void save(ProductDto productDto);

    ProductDto findById(Long id);

    void deleteById(Long id);
}
