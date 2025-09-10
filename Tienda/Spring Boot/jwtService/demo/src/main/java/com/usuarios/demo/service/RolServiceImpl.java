package com.usuarios.demo.service;

import com.usuarios.demo.model.Rol;
import com.usuarios.demo.model.cruddao.IRolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RolServiceImpl implements IRolService {

    @Autowired
    private IRolDao rolDao;

    @Override
    public Rol save(Rol rol) {
        if (!rol.getAuthority().startsWith("ROLE_")) {
            rol.setAuthority("ROLE_" + rol.getAuthority().toUpperCase());
        }
        return rolDao.save(rol);
    }

}
