package com.usuarios.demo.service;

import java.util.Optional;

import com.usuarios.demo.model.Usuario;

public interface IUsuarioService {
    Usuario save(Usuario usuario);
    Optional<Usuario> getUsuarioByUsername(String username);
}
