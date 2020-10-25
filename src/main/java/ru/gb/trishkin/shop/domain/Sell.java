package ru.gb.trishkin.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sell {
    private Long id;
    private LocalDateTime created;
    private Long orderId;
    private String userName;
    private String productTitle;
    private Long amount;
    private BigDecimal sum;

    public Sell(LocalDateTime created, Long orderId, String userName, String productTitle, Long amount, BigDecimal sum) {
        this.created = created;
        this.orderId = orderId;
        this.userName = userName;
        this.productTitle = productTitle;
        this.amount = amount;
        this.sum = sum;
    }
}
