package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Inventory;
import org.example.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    public Optional<Inventory> findByProduct(Product product);
}
