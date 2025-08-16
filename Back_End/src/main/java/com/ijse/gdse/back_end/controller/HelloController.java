package com.ijse.gdse.back_end.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String hello() {
        return "Hello World - Admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Hello World - User";
    }

    @GetMapping("/volunteer")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public String helloVolunteer() {
        return "Hello World - Victim";
    }

    @GetMapping("/donor")
    @PreAuthorize("hasRole('DONOR')")
    public String helloDonor() {
        return "Hello World - Donor";
    }




}
