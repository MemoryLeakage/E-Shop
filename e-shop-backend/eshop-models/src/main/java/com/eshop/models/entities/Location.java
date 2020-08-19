package com.eshop.models.entities;


import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "latitude")
    private Long latitude;
    @Column(name = "longitude")
    private Long longitude;
    @Column(name = "address")
    private String address;

}
