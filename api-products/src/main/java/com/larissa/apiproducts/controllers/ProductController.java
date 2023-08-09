package com.larissa.apiproducts.controllers;

import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import com.larissa.apiproducts.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
        ProductModel product = productService.saveNewProduct(productRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductModel>> getAll(@RequestParam int pageSize) {
        Page<ProductModel> products = productService.getProducts(pageSize);
        if (!products.isEmpty()) {
            for (ProductModel product : products) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id, 1)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id,
                                                @RequestParam(defaultValue = "0") int pageSize) {
        Optional<ProductModel> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        product.get().add(linkTo(methodOn(ProductController.class).getAll(pageSize)).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductModel>> getByProductName(@RequestParam String productName) {
        List<ProductModel> product = productService.findByName(productName);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> optionalProduct = productService.updateAProduct(id, productRecordDto);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalProduct.get());

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        boolean deletedProduct = productService.deleteProduct(id);

        if (!deletedProduct) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted");
    }

}
