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
@Table(name="Service")
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int service_id;

    @Basic
    @Column(name = "service_name")
    private String service_name;

    @Basic
    @Column(name = "unit")
    private String unit;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "category_id")
    private String category_id;

//    @Basic
//    @ManyToMany(mappedBy = "Service")
//    private Collection<Discount> discounts;

}
