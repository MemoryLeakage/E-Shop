package com.eshop.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.eshop.models.IdsGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;


    public String getName() {
        return name;
    }

    public Category() {
    }

    public Category(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return getId().equals(category.getId()) && name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }

    @Override
    public String toString() {
        return "{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }
}
