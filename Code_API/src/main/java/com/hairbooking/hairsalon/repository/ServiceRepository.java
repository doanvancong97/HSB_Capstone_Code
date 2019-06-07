package com.hairbooking.hairsalon.repository;

import com.hairbooking.hairsalon.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

}
