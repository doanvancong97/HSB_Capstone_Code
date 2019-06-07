package com.hairbooking.hairsalon.impl;

import com.hairbooking.hairsalon.dto.SalonServiceDTO;
import com.hairbooking.hairsalon.repository.SalonRepository;
import com.hairbooking.hairsalon.service.SalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonServiceImpl implements SalonService {

    @Autowired
    private final SalonRepository salonRepository;

    public SalonServiceImpl(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }


    @Override
    public List<SalonServiceDTO> getAllSalons() {
        return null;
    }
}
