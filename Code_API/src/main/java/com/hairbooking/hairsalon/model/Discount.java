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
@Table(name="Discount")
public class Discount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int discountId;

    @Basic
    @Column(name = "discount_value")
    private String discountValue;

    @Basic
    @Column(name = "discount_unit")
    private String discountUnit;

    @Basic
    @Column(name = "create_date")
    private String createDate;

    @Basic
    @Column(name = "valid_from")
    private String validFrom;

    @Basic
    @Column(name = "valid_util")
    private String validUntil;

    @Basic
    @Column(name = "coupon_code")
    private String couponCode;

    @Basic
    @Column(name = "minimum_booking_value")
    private String minimumBookingValue;

    @Basic
    @Column(name = "maximum_discount_amount")
    private String maximumDiscountAmount;

    @Basic
    @Column(name = "status")
    private String status;

//    @Basic
//    @ManyToMany
//    @JoinTable(name = "SalonService",
//            joinColumns = @JoinColumn(name = "id") ,
//            inverseJoinColumns = @JoinColumn(name = "service_id"))
//    private Collection<Service> services;
}
