package com.productos.productos.service;

import com.productos.productos.model.DetalleOrden;
import com.productos.productos.model.Productos;
import com.productos.productos.model.dao.OrdenDAO;
import com.productos.productos.model.dao.ProductoDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.productos.productos.model.Orden;
import com.productos.productos.response.OrdenRequestDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenService {
    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private OrdenDAO ordenDAO;


    public Orden crearOrden(OrdenRequestDTO request) throws IOException {
        Orden orden = new Orden();
        orden.setUsername(request.getUsername());
        orden.setTotal(request.getTotal());
        orden.setUrlPDF(request.getUrlPDF());

        List<DetalleOrden> detalles = new ArrayList<>();

        request.getProductos().stream().forEach((x) -> {
            Productos producto = productoDao.findById(x.getProductoId()).orElseThrow(() -> new RuntimeException("Producto no encontrado: " + x.getProductoId()));
            DetalleOrden detalle = new DetalleOrden();
            detalle.setProducto(producto);
            detalle.setCantidad(x.getCantidad());
            detalle.setOrden(orden);
            detalles.add(detalle);
        });

        orden.setDetalles(detalles);

        Orden ordenGuardada = ordenDAO.save(orden);

        return  ordenGuardada;

    }


}
