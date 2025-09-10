package com.usuarios.demo.model.cruddao;

import com.usuarios.demo.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario,Long> {}
