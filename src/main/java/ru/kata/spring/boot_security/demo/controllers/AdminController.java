package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.AdminValidator;
import ru.kata.spring.boot_security.demo.Util.DeleteValid;
import ru.kata.spring.boot_security.demo.configs.Authentication;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminValidator adminValidator;
    private final RoleService roleService;
    private final DeleteValid deleteValid;

    @Autowired
    public AdminController(UserService userService,
                           AdminValidator adminValidator,
                           RoleService roleService,
                           Authentication authentication, DeleteValid deleteValid) {
        this.userService = userService;
        this.adminValidator = adminValidator;
        this.roleService = roleService;
        this.deleteValid = deleteValid;
    }

    @GetMapping()
    public String listUsers(@ModelAttribute("user") User user,
                            Model model) {
        model.addAttribute("users", userService.listAll());
        return "admin/users";
    }


    @GetMapping("/edit")
    public String registrationPage(@RequestParam("id") int id,
                                   Model model) {
        User user1 = userService.get(id);
        model.addAttribute("user", userService.get(id));
        model.addAttribute("roles", roleService.listAll());
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String actRegistration(@ModelAttribute("user") User user,
                                  Principal principal,
                                  Model model,
                                  BindingResult bindingResult) {
        User authUser = (User) userService.loadUserByUsername(principal.getName());
        adminValidator.validateWithPrincipal(user, bindingResult, authUser);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.listAll());
            return "admin/edit";
        }

        userService.save(user, false);

        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") User user, Model model) {

        model.addAttribute("roles", roleService.listAll());
        return "admin/add";
    }

    @PostMapping("/add")
    public String actAdding(@ModelAttribute("user") User user,
                            Model model,
                            BindingResult bindingResult) {

        adminValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.listAll());
            return "admin/add";
        }

        userService.save(user, true);

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String actRegistration(@ModelAttribute("user") @Valid User user,
                                  Model model,
                                  BindingResult bindingResult,
                                  Principal principal) {
User authUser = (User) userService.loadUserByUsername(principal.getName());
        deleteValid.validateWithPrincipal(user, bindingResult, authUser);

        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.listAll());
            return "admin/users";
        }
        userService.delete(user.getId());

        return "redirect:/admin";
    }
}