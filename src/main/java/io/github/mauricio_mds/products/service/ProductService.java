package io.github.mauricio_mds.products.service;

import io.github.mauricio_mds.products.rest.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel) throws Exception;
}
