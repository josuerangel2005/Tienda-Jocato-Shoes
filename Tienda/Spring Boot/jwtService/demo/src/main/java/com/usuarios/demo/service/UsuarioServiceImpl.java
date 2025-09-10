package com.usuarios.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usuarios.demo.model.Usuario;
import com.usuarios.demo.model.cruddao.IUsuarioDao;


@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario save(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);
        return usuarioDao.save(usuario);
    }

    @Override
    public Optional<Usuario> getUsuarioByUsername(String username) {
    List<Usuario> usuarios = new ArrayList<>();

    usuarioDao.findAll().forEach((Usuario user) -> usuarios.add(user));

    Optional<Usuario> usuarioP = usuarios.stream()
        .filter((Usuario u) -> u.getUsername().equals(username))
        .findFirst();
        
        return usuarioP;
}


}
