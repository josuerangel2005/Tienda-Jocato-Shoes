package com.productos.productos.response;

import java.util.List;

public class OrdenRequestDTO {

    private String username;
    private String direccion;
    private String nombre;
    private String apellido;
    private String documento;
    private Double total;
    private String urlPDF;
    private List<productoCantidadDTO> productos;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<productoCantidadDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<productoCantidadDTO> productos) {
        this.productos = productos;
    }

    public String getUrlPDF() {
        return urlPDF;
    }

    public void setUrlPDF(String urlPDF) {
        this.urlPDF = urlPDF;
    }
}
