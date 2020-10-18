package ru.gb.trishkin.shop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.trishkin.shop.aop.aspect.MeasureMethod;
import ru.gb.trishkin.shop.domain.Product;
import ru.gb.trishkin.shop.dto.ProductDto;
import ru.gb.trishkin.shop.dto.UserDto;
import ru.gb.trishkin.shop.service.ProductService;
import ru.gb.trishkin.shop.service.SessionObjectHolder;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SessionObjectHolder sessionObjectHolder;


    public ProductController(ProductService productService, SessionObjectHolder sessionObjectHolder) {
        this.productService = productService;
        this.sessionObjectHolder = sessionObjectHolder;
    }

    @MeasureMethod
    @GetMapping
    public String list(Model model) {
        sessionObjectHolder.addClick();
        List<ProductDto> list = productService.getAll();
        model.addAttribute("products", list);
        return "product";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal) {
        sessionObjectHolder.addClick();
        if (principal == null) {
            return "redirect:/products";
        }
        productService.addToUserBucket(id, principal.getName());
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/new")
    public String newProduct(Model model) {
        System.out.println("Called method newProduct");
        model.addAttribute("product", new ProductDto());
        return "new-product";
    }

    @PostMapping(value = "/new")
    public String saveProduct(ProductDto dto, Model model) {
        productService.save(dto);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        System.out.println(productService.findById(id).getTitle());
        model.addAttribute("product", productService.findById(id));
        return "edit_product";
    }

    @PostMapping("/edit")
    public String modifyProduct(ProductDto productDto) {
        productService.save(productDto);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteById(id);
        return "redirect:/products";
    }
}
