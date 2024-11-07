package org.example.ecommerce.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "e_order")
public class Order extends BaseModel{
    @ManyToOne
    private User user;
    @ManyToOne
    private Address deliveryAddress;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
