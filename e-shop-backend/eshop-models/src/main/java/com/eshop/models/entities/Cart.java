package com.eshop.models.entities;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.eshop.models.IdsGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "number_of_items")
    private Integer numOfItems;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    private List<CartItem> cartItemList;

    public Cart() {
    }

    public Cart(Integer numOfItems, Double totalPrice, User user) {
        this.totalPrice = totalPrice;
        this.numOfItems = numOfItems;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Integer getNumOfItems() {
        return numOfItems;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setNumOfItems(Integer numOfItems) {
        this.numOfItems = numOfItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return getId().equals(cart.getId()) &&
                Objects.equals(getTotalPrice(), cart.getTotalPrice()) &&
                Objects.equals(getNumOfItems(), cart.getNumOfItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTotalPrice(), getNumOfItems());
    }
}
