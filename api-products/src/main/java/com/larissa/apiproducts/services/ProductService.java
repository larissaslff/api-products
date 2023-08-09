package com.larissa.apiproducts.services;

import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {
    public Page<ProductModel> getProducts(int page, int size);

    public Optional<ProductModel> getProductById(UUID id);

    public ProductModel saveNewProduct(ProductRecordDto productDto);

    public Optional<ProductModel> updateAProduct(UUID id, ProductRecordDto productRecordDto);

    public boolean deleteProduct(UUID id);
    List<ProductModel> findByName(String productName);
}
