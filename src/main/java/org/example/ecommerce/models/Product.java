package org.example.ecommerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "e_product")
public class Product extends BaseModel{
    private String name;
    private String description;
    private double price;
}
