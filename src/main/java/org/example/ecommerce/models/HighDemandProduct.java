package org.example.ecommerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class HighDemandProduct extends BaseModel{
    @ManyToOne
    private Product product;
    private int maxQuantity;
}

