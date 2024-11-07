package org.example.ecommerce.repositories;


import org.example.ecommerce.models.Order;
import org.example.ecommerce.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    public List<OrderDetail> findAllByOrder(Order order);
    public void deleteAll();
}