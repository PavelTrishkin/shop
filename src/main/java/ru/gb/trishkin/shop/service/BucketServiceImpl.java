package ru.gb.trishkin.shop.service;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.gb.trishkin.shop.config.SellIntegrationConfig;
import ru.gb.trishkin.shop.dao.BucketRepository;
import ru.gb.trishkin.shop.dao.ProductRepository;
import ru.gb.trishkin.shop.domain.*;
import ru.gb.trishkin.shop.dto.BucketDetailDto;
import ru.gb.trishkin.shop.dto.BucketDto;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final SimpMessagingTemplate template;
    private final OrderService orderService;
    private final SellIntegrationConfig config;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService, SimpMessagingTemplate template, OrderService orderService, SellIntegrationConfig config) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.template = template;
        this.orderService = orderService;
        this.config = config;
    }

    @Override
    @Transactional
    public void commitBucketToOrder(String username) {
        User user = userService.findByName(username);
        if (user == null){
            throw new RuntimeException("User not found!");
        }
        Bucket bucket = user.getBucket();
        if (bucket == null || bucket.getProducts().isEmpty()){
            return;
        }

        Order order = new Order();
        order.setOrderStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = bucket.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setOrderDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");

        orderService.saveOrder(order);
        createAndSendSellList(orderDetails, order, config.getSellsChannel());
        bucket.getProducts().clear();
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProducts(Bucket bucket, List<Long> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductsList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductsList);
        bucketRepository.save(bucket);
        BucketDto bucketDto = getBucketByUser(bucket.getUser().getName());
        template.convertAndSend("/topic/bucket", bucketDto);
    }

    @Override
    public BucketDto getBucketByUser(String name) {
        User user = userService.findByName(name);
        if(user == null || user.getBucket() == null){
            return new BucketDto();
        }

        BucketDto bucketDto = new BucketDto();
        Map<Long, BucketDetailDto> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailDto detail = mapByProductId.get(product.getId());
            if(detail == null){
                mapByProductId.put(product.getId(), new BucketDetailDto(product));
            }
            else {
                detail.setAmount(detail.getAmount() + 1.0);
                detail.setSum(detail.getSum() + product.getPrice());
            }
        }

        bucketDto.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDto.aggregate();

        return bucketDto;
    }

    //Создание и отправка списка продаж
    public void createAndSendSellList(List<OrderDetails> orderDetails, Order order, DirectChannel channel){
        SellsList sellsList = new SellsList();
        List<Sell> sells = new ArrayList<>();

        for (OrderDetails o : orderDetails) {
            sells.add(new Sell(LocalDateTime.now(),order.getId(),order.getUser().getName(), o.getProduct().getTitle(), o.getAmount().longValue(), o.getAmount().multiply(o.getPrice())));
        }
        sellsList.setSell(sells);
        Message<SellsList> message = MessageBuilder.
                withPayload(sellsList)
                .setHeader("Content-type", "application/json")
                .build();
        channel.send(message);
        System.out.println("Sell list: " + message);
    }
}