package ru.gudoshnikova.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gudoshnikova.mainproject.model.User;
import ru.gudoshnikova.mainproject.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String getAccount(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user= userService.findByUsername(userDetail.getUsername());

        model.addAttribute("user", user);
        return "account/mainAccount";
    }
    @GetMapping("/orders")
    public String orders(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user= userService.findByUsername(userDetail.getUsername());
        model.addAttribute("user", user);
        return "account/orders";
    }
    @GetMapping("/edit")
    public String edit(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user= userService.findByUsername(userDetail.getUsername());
        model.addAttribute("user", user);
        return "account/edit";
    }
    @PatchMapping("/edit")
    public String edit(Model model, @ModelAttribute("user") User user){
        userService.save(user);
        return "account/mainAccount";
    }
}
