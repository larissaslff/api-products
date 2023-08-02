package com.larissa.apiproducts.services;

import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<ProductModel> getProducts() {
        List<ProductModel> allProducts = productRepository.findAll();
        return allProducts;
    }

    public Optional<ProductModel> getProductById(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        return product;
    }
}
