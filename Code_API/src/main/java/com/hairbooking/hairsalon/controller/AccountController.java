package com.hairbooking.hairsalon.controller;

import com.hairbooking.hairsalon.model.Account;
import com.hairbooking.hairsalon.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping("/users")
    public List<Account> getAllUser() {
        System.out.println(accountService.getAllUser());
        return accountService.getAllUser();
    }

    @GetMapping("/users/{id}")
    public Optional<Account> findById(@PathVariable(value = "id") int id) {
        return accountService.findById(id);
    }
}
