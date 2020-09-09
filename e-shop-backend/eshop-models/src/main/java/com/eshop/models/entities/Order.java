package com.eshop.models.entities;

import com.eshop.models.constants.DeliveryState;
import com.eshop.models.constants.PaymentMethod;
import com.eshop.models.constants.PaymentState;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "payment_state")
    private PaymentState paymentState;
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
}
