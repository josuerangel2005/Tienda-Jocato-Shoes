package com.productos.productos.service;

import com.productos.productos.model.Productos;
import com.productos.productos.model.dao.ProductoDao;
import com.productos.productos.response.ProductoResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    ProductoDao productoDao;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<ProductoResponseRest> listarProductos() {
        ProductoResponseRest productoResponseRest = new ProductoResponseRest();
        List<Productos> productos = new ArrayList<>();

        try {
            productoDao.findAll().forEach(x -> productos.add(x));
            productoResponseRest.getProductoResponse().setProductos(productos);
            productoResponseRest.setMetadata("Ok", new Date(), "200");
        } catch (Exception c) {
            productoResponseRest.setMetadata("noK", new Date(), "500");
            return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ProductoResponseRest> guardarProducto(Productos producto) {
        ProductoResponseRest productoResponseRest = new ProductoResponseRest();
        List<Productos> productos = new ArrayList<>();
        try {
            Productos productos1 = productoDao.save(producto);
            productos.add(productos1);
            productoResponseRest.getProductoResponse().setProductos(productos);
        } catch (Exception e) {
            productoResponseRest.setMetadata("nOK", new Date(), "500");
            return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.BAD_REQUEST);
        }
        productoResponseRest.setMetadata("0k", new Date(), "200");
        return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.OK);

    }

    @Transactional
    @Override
    public ResponseEntity<ProductoResponseRest> actualizarProducto(Productos productos, Long id) {
        ProductoResponseRest productoResponseRest = new ProductoResponseRest();
        List<Productos> productosList = new ArrayList<>();

        try {
            Optional<Productos> productoActualizar = productoDao.findById(id);

            if (productoActualizar.isPresent()) {
                Productos productos1 = productoActualizar.get();

                productos1.setDescripcion(productos.getDescripcion());
                productos1.setMarca(productos.getMarca());
                productos1.setNombre(productos.getNombre());
                productos1.setPrecio(productos.getPrecio());
                productos1.setUrlImage(productos.getUrlImage());

                Productos productoGuardar = productoDao.save(productos1);
                productosList.add(productoGuardar);
                productoResponseRest.getProductoResponse().setProductos(productosList);
            } else {
                productoResponseRest.setMetadata("noK", new Date(), "500");
                return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            productoResponseRest.setMetadata("oK", new Date(), "500");
            return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        productoResponseRest.setMetadata("oK", new Date(), "200");
        return new ResponseEntity<ProductoResponseRest>(productoResponseRest, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ProductoResponseRest> eliminarProducto(Long id) {
        ProductoResponseRest response = new ProductoResponseRest();

        try {
            List<Productos> productos = new ArrayList<>();
            Optional<Productos> productoEliminar = productoDao.findById(id);
            productoEliminar.ifPresent(x -> productos.add(x));
            response.getProductoResponse().setProductos(productos);
            productoDao.deleteById(id);
        } catch (Exception e) {
            response.setMetadata("noK", new Date(), "500");
            return new ResponseEntity<ProductoResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setMetadata("Ok", new Date(), "200");
        return new ResponseEntity<ProductoResponseRest>(response, HttpStatus.OK);
    }
}
