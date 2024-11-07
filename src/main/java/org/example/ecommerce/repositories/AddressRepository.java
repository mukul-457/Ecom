package org.example.ecommerce.repositories;

import org.example.ecommerce.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Integer> {
    public Optional<Address> findById(int id);
}
