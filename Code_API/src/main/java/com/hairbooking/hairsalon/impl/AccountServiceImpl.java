package com.hairbooking.hairsalon.impl;

import com.hairbooking.hairsalon.model.Account;
import com.hairbooking.hairsalon.repository.AccountRepository;
import com.hairbooking.hairsalon.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

//    @Override
//    public boolean createUser(User user) {
//        System.out.println(user.getUsername());
//        User checkExistedAccount = userRepository.findByUsername(user.getUsername());
//        if (checkExistedAccount != null) {
//            return false;
//        }
//        user.setPassword(user.getPassword());
//        userRepository.save(user);
//        return true;
//    }

    @Override
    public List<Account> getAllUser() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(int id) {
        return accountRepository.findById(id);
    }


}
