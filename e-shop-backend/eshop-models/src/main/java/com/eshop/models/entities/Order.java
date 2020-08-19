package com.eshop.models.entities;

import com.eshop.models.constants.DeliveryState;
import com.eshop.models.constants.PaymentMethod;
import com.eshop.models.constants.PaymentState;

import javax.persistence.*;

@Entity
@Table(name = "order")
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

}
