package com.productos.productos.service;

import com.productos.productos.model.Productos;
import com.productos.productos.response.ProductoResponseRest;
import org.springframework.http.ResponseEntity;

public interface ProductoService {
   ResponseEntity<ProductoResponseRest> listarProductos();
   ResponseEntity<ProductoResponseRest> guardarProducto(Productos producto);
   ResponseEntity<ProductoResponseRest> actualizarProducto(Productos productos, Long id);
   ResponseEntity<ProductoResponseRest> eliminarProducto(Long id);
}
