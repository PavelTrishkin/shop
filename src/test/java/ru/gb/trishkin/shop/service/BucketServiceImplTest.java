package ru.gb.trishkin.shop.service;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.gb.trishkin.shop.config.SellIntegrationConfig;
import ru.gb.trishkin.shop.dao.BucketRepository;
import ru.gb.trishkin.shop.dao.ProductRepository;
import ru.gb.trishkin.shop.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

public class BucketServiceImplTest {

    private BucketServiceImpl bucketService;
    private BucketRepository bucketRepository;
    private UserService userService;
    private OrderService orderService;
    private SellIntegrationConfig config;
    private DirectChannel channel;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All tests");
    }

    @BeforeEach
    void setUp(){
        System.out.println("Before each test");
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        SimpMessagingTemplate template = Mockito.mock(SimpMessagingTemplate.class);

        bucketRepository = Mockito.mock(BucketRepository.class);
        userService = Mockito.mock(UserService.class);
        orderService = Mockito.mock(OrderService.class);
        config = Mockito.mock(SellIntegrationConfig.class);
        channel = Mockito.mock(DirectChannel.class);

        bucketService = new BucketServiceImpl(bucketRepository, productRepository,userService, template,orderService,config);
    }

    @AfterEach
    void afterEach(){
        System.out.println("After each test");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("After All test");
    }

    @Test
    void checkException(){
        //have
        User user = null;

        //execute
        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bucketService.commitBucketToOrder(user.getName());
            }
        });
    }

    @Test
    void checkEmptyBucket(){
        //have
        Bucket bucket = new Bucket();
        List<Product> products = new ArrayList<>();
        bucket.setProducts(products);
        User user = User.builder().id(1L).name("Bob").role(Role.CLIENT).bucket(bucket).build();

        Mockito.when(userService.findByName(user.getName())).thenReturn(user);

        //execute
        bucketService.commitBucketToOrder(user.getName());

        //check
        Assertions.assertTrue(bucket.getProducts().isEmpty());
        Mockito.verify(orderService, times(0)).saveOrder(Mockito.any());
    }

    @Test
    void checkInvokeAllMethods(){
        //have
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().id(1L).title("Test1").price(10.00).build());
        products.add(Product.builder().id(2L).title("Test2").price(20.00).build());
        products.add(Product.builder().id(3L).title("Test3").price(30.00).build());
        User user = User.builder().id(1L).name("Bob").role(Role.CLIENT).bucket(new Bucket()).build();
        user.getBucket().setProducts(products);
        Mockito.when(userService.findByName(user.getName())).thenReturn(user);
        Mockito.when(config.getSellsChannel()).thenReturn(channel);

        //execute
        bucketService.commitBucketToOrder(user.getName());

        //check
        Mockito.verify(orderService, times(1)).saveOrder(Mockito.any());
        Mockito.verify(bucketRepository, times(1)).save(Mockito.any());
        Assertions.assertTrue(user.getBucket().getProducts().isEmpty());
    }

}
