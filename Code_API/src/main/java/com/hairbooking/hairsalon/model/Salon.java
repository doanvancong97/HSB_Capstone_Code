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
@Table(name="Salon")
public class Salon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salon_id")
    private int salon_id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "phone_number")
    private String phone_number;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "status")
    private String status;


    @Basic
    @Column(name = "manager_id")
    private int manager_id;

    @Basic
    @Column(name = "url")
    private String url;

    }
