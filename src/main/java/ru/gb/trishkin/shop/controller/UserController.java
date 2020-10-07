package ru.gb.trishkin.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.trishkin.shop.dto.UserDto;
import ru.gb.trishkin.shop.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newUser(Model model){
        model.addAttribute("user", new UserDto());
        return "user";
    }

    @PostMapping("/new")
    public String saveUser(@RequestBody UserDto dto, Model model){
        if(userService.save(dto)){
            return "redirect:/";
        }
        else {
            model.addAttribute("user", dto);
            return "user";
        }
    }

}
