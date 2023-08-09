package com.larissa.apiproducts.services;

import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.larissa.apiproducts.dtos.ConvertProduct.convertToModel;

@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    ProductRepository productRepository;

    public Page<ProductModel> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductModel> allProducts = productRepository.findAll(pageRequest);
        return allProducts;
    }

    public Optional<ProductModel> getProductById(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        return product;
    }

    public ProductModel saveNewProduct(ProductRecordDto productDto) {
        ProductModel product = new ProductModel();
        BeanUtils.copyProperties(productDto, product);
        ProductModel savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Optional<ProductModel> updateAProduct(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductModel updatedProduct = convertToModel(productRecordDto);
            updatedProduct.setIdProduct(id);
            ProductModel saved = productRepository.save(updatedProduct);
            return Optional.of(saved);
        } else {
            return product;
        }
    }

    public boolean deleteProduct(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return false;
        }

        productRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ProductModel> findByName(String productName) {
        List<ProductModel> product = productRepository.findByNameContainingIgnoreCase(productName);
        return product;
    }
}
