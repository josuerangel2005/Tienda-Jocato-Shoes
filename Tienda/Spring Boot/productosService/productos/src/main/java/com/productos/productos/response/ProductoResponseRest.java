package com.productos.productos.response;

public class ProductoResponseRest extends MetadataResponse{
    private ProductoResponse productoResponse = new ProductoResponse();

    public ProductoResponse getProductoResponse() {
        return productoResponse;
    }

    public void setProductoResponse(ProductoResponse productoResponse) {
        this.productoResponse = productoResponse;
    }
}
