package net.thumbtack.ldap.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/home")
    public String employee(Principal principal) {
        return "Welcome to the home page!";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/denied")
    public String admin(Principal principal) {
        return "Welcome to the admin page!";
    }
}