package com.larissa.apiproducts.services;

import com.larissa.apiproducts.dtos.ConvertProduct;
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


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.larissa.apiproducts.dtos.ConvertProduct.convertToModel;
import static com.larissa.apiproducts.dtos.ConvertProduct.convertToRecord;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService service;
    private ProductModel p1;
    private ProductRecordDto productRecordDto;
    private List<ProductModel> listaDeProdutos;

    @BeforeAll
    public void setUp() {
        UUID id = UUID.randomUUID();
        p1 = new ProductModel(id, "Panela elétrica", BigDecimal.valueOf(250.00));
        listaDeProdutos = List.of(p1);
        productRecordDto = new ProductRecordDto(p1.getName(), p1.getValue());
    }
    @Test
    public void shouldShowAllProducts() {
        when(productRepository.findAll()).thenReturn(listaDeProdutos);
        List<ProductModel> products = service.getProducts();

        assertEquals(1, products.size());
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
}
