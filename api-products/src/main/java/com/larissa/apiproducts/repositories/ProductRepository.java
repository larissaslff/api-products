package com.larissa.apiproducts.repositories;

import com.larissa.apiproducts.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<ProductModel, UUID> {
    List<ProductModel> findByNameContainingIgnoreCase(String name);
}
