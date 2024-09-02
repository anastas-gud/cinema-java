package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.User;
import ru.gudoshnikova.mainproject.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> person = userRepository.findByLogin(username);
        System.out.println(userRepository.findAll());
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(person.get().getRole());
        return new org.springframework.security.core.userdetails.User(person.get().getUsername(),
                person.get().getPassword(), Collections.singletonList(authority));
    }
}
