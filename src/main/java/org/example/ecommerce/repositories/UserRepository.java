package org.example.ecommerce.repositories;

import org.example.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findById(int id );
}
