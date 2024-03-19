package ru.kata.spring.boot_security.demo.Util;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class DeleteValid {

    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    public void validateWithPrincipal(Object target, Errors errors, User authUser) {
        User user = (User) target;

        try {
            if (user.getId() == authUser.getId() || user.getId() == 1) {
                errors.rejectValue("name", "", "Данного пользователя нельзя удалить." +
                        "Нажмите на уведомление, чтобы выйти из режима удаления");
            }
        } catch (UsernameNotFoundException ignored) {
            return;
        }
    }
}
