package com.eshop.models.entities;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;


//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//    private List<ProductCategory> productCategories;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }


}
