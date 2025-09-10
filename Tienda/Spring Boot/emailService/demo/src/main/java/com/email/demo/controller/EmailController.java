package com.email.demo.controller;

import com.email.demo.model.EmailRequest;
import com.email.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailRequest request) {
        try {
            emailService.enviarCorreo(request.getTo(), request.getSubject(), request.getHtml());
            return ResponseEntity.ok("Correo enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }
}
