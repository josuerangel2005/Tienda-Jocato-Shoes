package com.usuarios.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.demo.model.Usuario;
import com.usuarios.demo.service.IUsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    // Endpoint para crear un nuevo usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return this.usuarioService.save(usuario);
    }
    
    @GetMapping
    public Usuario obtenerUsuarioByUsername(@RequestParam String username){
    return this.usuarioService.getUsuarioByUsername(username).orElse(null);
    }

}
