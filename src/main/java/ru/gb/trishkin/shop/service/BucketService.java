package ru.gb.trishkin.shop.service;


import ru.gb.trishkin.shop.domain.Bucket;
import ru.gb.trishkin.shop.domain.User;
import ru.gb.trishkin.shop.dto.BucketDto;
import ru.gb.trishkin.shop.dto.ProductDto;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Long> productIds);

    void addProducts(Bucket bucket, List<Long> productIds);

//    void test(BucketDto dto);

    BucketDto getBucketByUser(String name);
}
