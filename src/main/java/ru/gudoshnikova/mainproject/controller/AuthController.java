package ru.gudoshnikova.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gudoshnikova.mainproject.model.User;
import ru.gudoshnikova.mainproject.service.RegistrationService;

import java.time.LocalDate;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("date", LocalDate.now());
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user,
                               BindingResult bindingResult) {
        try {
            registrationService.register(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("ОШИБКА: повторяющееся значение ключа нарушает ограничение уникальности")) {
                bindingResult.rejectValue("login", "login.duplicate", "Логин уже существует");
                return "/auth/registration";
            }
        }
        return "redirect:/auth/login";
    }
}
