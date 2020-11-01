package ru.gb.trishkin.shop.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.gb.trishkin.shop.dto.ProductDto;
import ru.gb.trishkin.shop.service.ProductService;
import ru.gb.trishkin.shop.service.SessionObjectHolder;
import ru.gb.trishkin.shop.service.UserService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;
    @MockBean
    private SessionObjectHolder sessionObjectHolder;

    private ProductDto dto1 = new ProductDto(1L, "Potato", 500.00);
    private ProductDto dto2 = new ProductDto(8L, "TestProduct", 99.99);

    @BeforeEach
    void setUp(){
        given(productService.getAll()).willReturn(Arrays.asList(dto1, dto2));
    }

    @Test
    void checkList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + dto1.getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + dto2.getTitle() + "</td>")));
    }
}
