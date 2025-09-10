package com.productos.productos.controller;

import com.productos.productos.response.OrdenRequestDTO;
import com.productos.productos.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/orden/pdf")
public class PDFController {

    @Autowired
    PDFService pdfService;

    @PostMapping
    public String generarPDF(@RequestBody OrdenRequestDTO orden){
        try {
            byte[] pdfBytes = this.pdfService.generarFactura(orden);
            return Base64.getEncoder().encodeToString(pdfBytes);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
