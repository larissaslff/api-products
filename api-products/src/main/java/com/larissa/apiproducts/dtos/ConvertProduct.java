package com.larissa.apiproducts.dtos;

import com.larissa.apiproducts.models.ProductModel;

public abstract class ConvertProduct {
    public static ProductModel convertToModel (ProductRecordDto productRecordDto) {
        ProductModel productModel = new ProductModel(productRecordDto.name(), productRecordDto.value());
        return productModel;
    }
}
