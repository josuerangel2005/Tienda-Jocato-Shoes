package com.usuarios.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.demo.model.Rol;
import com.usuarios.demo.service.IRolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private IRolService rolService;

    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.save(rol);
    }
}
