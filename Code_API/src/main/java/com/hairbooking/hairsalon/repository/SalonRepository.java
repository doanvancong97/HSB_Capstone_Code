package com.hairbooking.hairsalon.repository;

import com.hairbooking.hairsalon.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonRepository extends JpaRepository<Account, Integer> {

}
