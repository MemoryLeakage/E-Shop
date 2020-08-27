package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "size")
    private Long size;
    @Column(name = "path")
    private String path;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_product_id", nullable = false)
    private Product product;

    public Long getId() {
        return id;
    }

    public Long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Product getProduct() {
        return product;
    }
}
