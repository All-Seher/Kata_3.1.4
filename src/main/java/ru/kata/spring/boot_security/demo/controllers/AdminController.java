package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.AdminValidator;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final AdminValidator adminValidator;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService, AdminValidator adminValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.adminValidator = adminValidator;
    }

    @GetMapping()
    public String listUsers(@ModelAttribute("user") User user,
                            Principal principal,
                            Model model) {

        User authUser = (User) userService.loadUserByUsername(principal.getName());

        model.addAttribute("users", userService.listAll());
        model.addAttribute("roles", roleService.listAll());
        model.addAttribute("authUser", authUser);

        return "admin/users";
    }

    @PostMapping("/edit/{id}")
    public String actRegistration(@ModelAttribute("user") User user) {

        userService.save(user, false);

        return "redirect:/admin";
    }

    @GetMapping("/delete{id}")
    public String act(@ModelAttribute("user") @Valid User user,
                      @RequestParam("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String actAdding(@ModelAttribute("user") User user,
                            Model model,
                            BindingResult bindingResult) {

        adminValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.listAll());
            return  "redirect:/admin";
        }

        userService.save(user, true);

        return "redirect:/admin";
    }
}