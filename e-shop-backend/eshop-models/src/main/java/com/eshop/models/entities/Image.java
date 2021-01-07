package com.eshop.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.eshop.models.IdsGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "size")
    private Integer size;
    @Column(name = "path")
    private String path;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Image() {
    }

    public String getId() {
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
        private String imageId;

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

        public Builder id(String imageId) {
            this.imageId = imageId;
            return this;
        }

        public Image build() {
            Image image = new Image();
            image.id = this.imageId;
            image.name = this.name;
            image.path = this.path;
            image.product = this.product;
            image.size = this.size;
            return image;
        }

    }
}
