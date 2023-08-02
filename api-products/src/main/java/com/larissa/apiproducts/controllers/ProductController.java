package com.larissa.apiproducts.controllers;

import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import com.larissa.apiproducts.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAll() {
        List<ProductModel> products = productService.getProducts();
        if (!products.isEmpty()) {
            for (ProductModel product : products) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        product.get().add(linkTo(methodOn(ProductController.class).getAll()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProducti(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        productRepository.delete(product.get());

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
    }

}
