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
    private Integer size;
    @Column(name = "path")
    private String path;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_product_id", nullable = false)
    private Product product;

    private Image() {
    }

    public Long getId() {
        return id;
    }

    public Integer getSize() {
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

    public static class Builder {
        private Integer size;
        private String path;
        private String name;
        private Product product;

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Image build() {
            Image image = new Image();
            image.name = this.name;
            image.path = this.path;
            image.product = this.product;
            image.size = this.size;
            return image;
        }
    }
}
