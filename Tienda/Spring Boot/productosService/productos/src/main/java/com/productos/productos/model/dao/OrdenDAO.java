package com.productos.productos.model.dao;

import com.productos.productos.model.Orden;
import org.springframework.data.repository.CrudRepository;

public interface OrdenDAO extends CrudRepository<Orden,Long> {
}
