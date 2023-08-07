package com.larissa.apiproducts.controllers;

import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import com.larissa.apiproducts.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    private final String URI = "/products";
    private UUID id = UUID.randomUUID();
    private ProductModel product = new ProductModel(id, "Notebook", BigDecimal.valueOf(60.000));
    private List<ProductModel> productsList = List.of(product);
    @Test
    public void shouldReturnProductsList() throws Exception {

        when(productService.getProducts()).thenReturn(productsList);

        mvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].value").value(BigDecimal.valueOf(60.000)));
    }

}