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
@Table(name="Account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "full_name")
    private String fullname;

    @Basic
    @Column(name = "birthdate")
    private String birthdate;

    @Basic
    @Column(name = "email")
    private String email;


    @Basic
    @Column(name = "job")
    private String job;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;

    @Basic
    @Column(name = "role_id")
    private int roleId;

    @Basic
    @Column(name = "avatar_url")
    private String avatar;

    @Basic
    @Column(name = "status")
    private String status;
}
