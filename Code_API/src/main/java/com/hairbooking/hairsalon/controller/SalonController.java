package com.hairbooking.hairsalon.controller;

import com.hairbooking.hairsalon.dto.SalonServiceDTO;
import com.hairbooking.hairsalon.service.SalonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SalonController {
    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }
    @GetMapping("/salon-service")
    public List<SalonServiceDTO> getAllSalons() {

        return salonService.getAllSalons();
    }
}
