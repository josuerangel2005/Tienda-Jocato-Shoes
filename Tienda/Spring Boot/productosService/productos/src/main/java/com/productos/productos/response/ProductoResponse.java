package com.productos.productos.response;

import com.productos.productos.model.Productos;

import java.util.ArrayList;
import java.util.List;

public class ProductoResponse {
    private List<Productos> productos = new ArrayList<>();

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }
}
