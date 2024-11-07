package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product , Integer> {
    Optional<Product> findById(int id );
}
