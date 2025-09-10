package com.productos.productos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.productos.model.Productos;
import com.productos.productos.response.ProductoResponseRest;
import com.productos.productos.service.ProductoService;

@RestController
@RequestMapping("/v4")// http://localhost/8080/v4/productos
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/productos")
    public ResponseEntity<ProductoResponseRest> obtenerProductos(){
        return productoService.listarProductos();
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoResponseRest> guardarProducto(@RequestBody Productos producto){
        return productoService.guardarProducto(producto);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoResponseRest> actualizarProducto(@RequestBody Productos producto, @PathVariable Long id){
        return productoService.actualizarProducto(producto,id);
    }

    @DeleteMapping("/productos/{id}")
    public  ResponseEntity<ProductoResponseRest> eliminarProducto(@PathVariable Long id){
        return productoService.eliminarProducto(id);
    }
}
