package ru.kata.spring.boot_security.demo.Util;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");

        Role adminRole = roleRepository.findByName("ADMIN");
       User user = createUserIfNotFound("1", "1", "1");
        user.setRoles(Set.of(adminRole));
    }

    @Transactional
    public Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }

        return role;
    }

    @Transactional
    public User createUserIfNotFound(String name, String surName, String Password) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            user = Optional.of(new User(name, surName, passwordEncoder.encode("1")));
            userRepository.save(user.get());
        }

        return user.get();
    }
}
