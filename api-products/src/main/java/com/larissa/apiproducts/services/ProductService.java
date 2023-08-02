package com.larissa.apiproducts.services;

import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<ProductModel> getProducts() {
        List<ProductModel> allProducts = productRepository.findAll();
        return allProducts;
    }
}
