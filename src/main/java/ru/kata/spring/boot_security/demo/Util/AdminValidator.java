package ru.kata.spring.boot_security.demo.Util;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class AdminValidator implements Validator {

    private final UserService userService;

    public AdminValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    public void validateWithPrincipal(Object target, Errors errors, User authUser) {
        User user = (User) target;

        try {
            if (user.getId() == authUser.getId() || user.getId() == 1) {
                errors.rejectValue("name", "", "Данного пользователя нельзя редактивроть");
            }
        } catch (UsernameNotFoundException ignored) {
            return;
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        User userFromRepo;
        User user = (User) target;
        int i = user.getId();
        try {
            userFromRepo = (User) userService.loadUserByUsername(user.getName());
            if (user.getId() == userFromRepo.getId() && user.getId() != 1) {
                return;
            }
        } catch (UsernameNotFoundException ignored) {
            return;
        }


        errors.rejectValue("name", "", "Данного пользователя нельзя редактировать");
    }
}
