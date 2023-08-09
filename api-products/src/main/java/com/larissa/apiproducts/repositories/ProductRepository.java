package com.larissa.apiproducts.repositories;

import com.larissa.apiproducts.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    List<ProductModel> findByNameContainingIgnoreCase(String name);
}
