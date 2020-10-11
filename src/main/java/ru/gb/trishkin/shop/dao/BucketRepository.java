package ru.gb.trishkin.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.trishkin.shop.domain.Bucket;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
}
