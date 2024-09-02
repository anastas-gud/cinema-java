package ru.gudoshnikova.mainproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import ru.gudoshnikova.mainproject.service.UserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final UserDetailService userDetailService;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public NoOpPasswordEncoder bCryptPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((request)->request
                        .requestMatchers("admin/films").hasRole("ADMIN")
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/auth/registration", "/auth/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form)->form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/films", true)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .permitAll());
        return http.build();


//        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
//                //Настройка http запросов - кому куда можно/нельзя
//                .authorizeHttpRequests((requests) ->
//                        requests
//                                .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
//
//                                .requestMatchers(FILMS_WHITE_LIST.toArray(String[]::new)).permitAll()
//                                .requestMatchers(ORDERS_WHITE_LIST.toArray(String[]::new)).authenticated()
//                                .requestMatchers(REVIEWS_WHITE_LIST.toArray(String[]::new)).hasRole(USER)
//                                .requestMatchers(SEATS_WHITE_LIST.toArray(String[]::new)).permitAll()
//                                .requestMatchers(USERS_WHITE_LIST.toArray(String[]::new)).authenticated()
//
//                                .requestMatchers(FILMS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(FILM_CREATORS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(ORDERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(FILM_SESSIONS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(REVIEWS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(SEATS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole, MANAGER)
//                                .requestMatchers(USERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(adminRole)
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/")
//                        .permitAll()
//                )
//                .logout((logout) -> logout
//                        .logoutSuccessUrl("/login")
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                );
//        return http.build();
    }
}
