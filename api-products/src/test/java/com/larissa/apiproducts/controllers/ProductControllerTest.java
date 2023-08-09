package com.larissa.apiproducts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import com.larissa.apiproducts.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.larissa.apiproducts.dtos.ConvertProduct.convertToRecord;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    private ObjectMapper objectMapper;
    private final String URI = "/products";
    private final UUID ID = UUID.randomUUID();
    private int pageSize = 2;
    private int pageNumber = 0;
    private final BigDecimal VALUE = BigDecimal.valueOf(60.000);
    private ProductModel product = new ProductModel(ID, "Notebook", VALUE);
    private ProductRecordDto productRecordDto = convertToRecord(product);
    private List<ProductModel> productsList = List.of(product);

    @Test
    public void shouldReturnProductsList() throws Exception {
        Page<ProductModel> mockPage = new PageImpl<>(productsList);

        when(productService.getProducts(pageNumber, pageSize)).thenReturn(mockPage);

        mvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].value").value(product.getValue().toString()));
    }

    @Test
    public void shouldReturnProductById() throws Exception {
        when(productService.getProductById(ID)).thenReturn(Optional.of(product));

        mvc.perform(get(URI + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.value").value(product.getValue()))
                .andExpect(jsonPath("$.idProduct").value(product.getIdProduct().toString()));
    }

    @Test
    public void shouldNotReturnAnyProductBecauseTheIdDoesNotExist() throws Exception {
        UUID randomID = UUID.randomUUID();
        mvc.perform(get(URI + "/{id}", randomID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Product not found."));
    }

    @Test
    public void shouldSaveANewProduct() throws Exception {
        when(productService.saveNewProduct(productRecordDto)).thenReturn(product);

        mvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRecordDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateProductWhenTheIdExists() throws Exception {
        ProductModel updatedProduct = new ProductModel(ID, "Notebook 2.0", product.getValue());
        ProductRecordDto productToUpdate = convertToRecord(updatedProduct);

        when(productService.updateAProduct(ID, productToUpdate)).thenReturn(Optional.of(updatedProduct));

        mvc.perform(put(URI + "/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.value").value(updatedProduct.getValue()))
                .andExpect(jsonPath("$.idProduct").value(updatedProduct.getIdProduct().toString()));
    }

    @Test
    public void shouldNotUpdateProductWhenTheIdDoesNotExists() throws Exception {
        ProductModel updatedProduct = new ProductModel(ID, "Notebook 2.0", product.getValue());
        ProductRecordDto productToUpdate = convertToRecord(updatedProduct);

        UUID randomUUID = UUID.randomUUID();
        when(productService.updateAProduct(randomUUID, productToUpdate)).thenReturn(Optional.empty());

        mvc.perform(put(URI + "/{id}", randomUUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Product not found."));
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        when(productService.deleteProduct(ID)).thenReturn(true);

        mvc.perform(delete(URI + "/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotDeleteProduct() throws Exception {
        when(productService.deleteProduct(ID)).thenReturn(false);

        mvc.perform(delete(URI + "/{id}", ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Product not found."));
    }

    @Test
    public void shouldReturnAListWithProduct() throws Exception {
        when(productService.findByName(product.getName())).thenReturn(List.of(product));

        mvc.perform(get(URI + "/search").param("productName", product.getName()))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }

}