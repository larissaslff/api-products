package com.larissa.apiproducts.repositories;

import com.larissa.apiproducts.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    @Override
    Page<ProductModel> findAll(Pageable pageable);

    List<ProductModel> findByNameContainingIgnoreCase(String name);
}
