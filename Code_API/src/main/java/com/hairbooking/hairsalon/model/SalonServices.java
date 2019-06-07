package com.hairbooking.hairsalon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="Salon_Services")
public class SalonServices implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "salon_id")
    private String salonId;

    @Basic
    @Column(name = "service_id")
    private String serviceId;

    @Basic
    @Column(name = "discount_id")
    private String discountId;

    @Basic
    @Column(name = "price")
    private String price;

}
