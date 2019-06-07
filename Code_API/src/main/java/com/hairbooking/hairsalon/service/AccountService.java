package com.hairbooking.hairsalon.service;

import com.hairbooking.hairsalon.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllUser();
    Optional<Account> findById(int id);
}
