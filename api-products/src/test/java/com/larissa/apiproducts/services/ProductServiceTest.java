package com.larissa.apiproducts.services;

import com.larissa.apiproducts.dtos.ProductRecordDto;
import com.larissa.apiproducts.models.ProductModel;
import com.larissa.apiproducts.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImplementation service;
    private ProductModel p1;
    private ProductRecordDto productRecordDto;
    private List<ProductModel> listaDeProdutos;
    private int pageSize = 2;
    private int pageNumber = 0;

    @BeforeAll
    public void setUp() {
        UUID id = UUID.randomUUID();
        p1 = new ProductModel(id, "Panela elétrica", BigDecimal.valueOf(250.00));
        listaDeProdutos = List.of(p1);
        productRecordDto = new ProductRecordDto(p1.getName(), p1.getValue());
    }

    @Test
    public void shouldShowAllProducts() {
        Page<ProductModel> page = new PageImpl<>(listaDeProdutos);
        when(productRepository.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(page);
        Page<ProductModel> products = service.getProducts(pageNumber, pageSize);

        assertEquals(1, products.getTotalElements());
        assertEquals(listaDeProdutos.get(0).getName(), p1.getName());
        assertEquals(listaDeProdutos.get(0).getValue(), p1.getValue());
    }

    @Test
    public void shouldShowOneProduct() {
        when(productRepository.findById(p1.getIdProduct())).thenReturn(Optional.ofNullable(p1));
        Optional<ProductModel> product = service.getProductById(p1.getIdProduct());

        assertTrue(product.isPresent());
        assertEquals(p1.getValue(), product.get().getValue());
        assertEquals(p1.getName(), product.get().getName());
    }

    @Test
    public void shouldSaveOneProduct() {
        when(productRepository.save(p1)).thenReturn(p1);

        ProductModel newProduct = service.saveNewProduct(productRecordDto);

        assertEquals(p1.getName(), newProduct.getName());
        assertEquals(p1.getIdProduct(), newProduct.getIdProduct());
        assertEquals(p1.getValue(), newProduct.getValue());
    }

    @Test
    public void shouldUpdateProduct() {
        ProductRecordDto productToUpdate = new ProductRecordDto(p1.getName(), BigDecimal.valueOf(50.000));
        ProductModel product = new ProductModel(p1.getIdProduct(), productToUpdate.name(), productToUpdate.value());

        when(productRepository.findById(p1.getIdProduct())).thenReturn(Optional.of(p1));

        when(productRepository.save(any(ProductModel.class))).thenReturn(product);

        Optional<ProductModel> productModel = service.updateAProduct(product.getIdProduct(), productToUpdate);

        assertEquals(product.getIdProduct(), productModel.get().getIdProduct());
        assertEquals(product.getName(), productModel.get().getName());
        assertEquals(BigDecimal.valueOf(50.000), productModel.get().getValue());
    }

    @Test
    public void shouldNotUpdateProduct() {
        UUID nonExistentId = UUID.randomUUID();

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<ProductModel> productNotFound = service.updateAProduct(nonExistentId, productRecordDto);

        assertTrue(productNotFound.isEmpty());

    }

    @Test
    public void shouldDelete(){
        when(productRepository.findById(p1.getIdProduct())).thenReturn(Optional.of(p1));

        boolean deletedProduct = service.deleteProduct(p1.getIdProduct());

        assertTrue(deletedProduct);
    }

    @Test
    public void shouldNotDelete(){
        UUID randomUUID = UUID.randomUUID();
        when(productRepository.findById(randomUUID)).thenReturn(Optional.empty());

        boolean deletedProduct = service.deleteProduct(randomUUID);

        assertFalse(deletedProduct);
    }

    @Test
    public void shouldReturnAProductWhenSearchedByName(){
        String productName = "Panela";
        when(productRepository.findByNameContainingIgnoreCase(productName)).thenReturn(List.of(p1));

        List<ProductModel> searchedByName = service.findByName(productName);

        assertEquals(1, searchedByName.size());
        assertEquals(p1.getName(), searchedByName.get(0).getName());
    }
}
