package com.hairbooking.hairsalon.repository;

import com.hairbooking.hairsalon.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AccountRepository extends JpaRepository <Account, Integer> {

}
