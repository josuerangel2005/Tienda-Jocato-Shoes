package com.productos.productos.model.dao;

import com.productos.productos.model.Productos;
import org.springframework.data.repository.CrudRepository;

public interface ProductoDao extends CrudRepository<Productos,Long> {}
