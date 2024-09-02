package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.User;
import ru.gudoshnikova.mainproject.repository.UserRepository;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardService cardService;
    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, CardService cardService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardService = cardService;
    }
    @Transactional
    public void register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setBonuses((double) 0);
        user.setCard(cardService.getCardById(1));
        user.setTotalSum((double) 0);
        userRepository.save(user);
    }
}
