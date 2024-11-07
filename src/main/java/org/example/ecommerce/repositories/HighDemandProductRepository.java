package org.example.ecommerce.repositories;

import org.example.ecommerce.models.HighDemandProduct;
import org.example.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HighDemandProductRepository extends JpaRepository<HighDemandProduct, Integer> {
    public Optional<HighDemandProduct> findByProduct(Product product);
}
