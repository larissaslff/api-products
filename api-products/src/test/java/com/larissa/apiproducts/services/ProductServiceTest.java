package com.larissa.apiproducts.services;

import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService service;
    private ProductModel p1;
    private List<ProductModel> listaDeProdutos;

    @BeforeAll
    public void setUp() {
        p1 = new ProductModel("Panela elétrica", BigDecimal.valueOf(250.00));
        listaDeProdutos = List.of(p1);

    }

    @Test
    public void getProducts() {
        when(productRepository.findAll()).thenReturn(listaDeProdutos);
        List<ProductModel> products = service.getProducts();

        assertEquals(1, products.size());

    }
}