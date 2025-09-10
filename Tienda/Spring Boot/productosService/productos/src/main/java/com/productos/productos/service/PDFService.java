package com.productos.productos.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.productos.productos.model.Productos;
import com.productos.productos.model.dao.ProductoDao;
import com.productos.productos.response.OrdenRequestDTO;
import com.productos.productos.response.productoCantidadDTO;

@Service
public class PDFService {

    @Autowired
    ProductoDao productoDao;

    public byte[] generarFactura(OrdenRequestDTO orden) throws IOException {
        PDDocument documento = new PDDocument();

        // Configuración inicial
        float marginLeft = 50;
        float lineHeight = 20;

        // Primera página
        PDPage pagina = new PDPage(PDRectangle.A4);
        documento.addPage(pagina);
        float pageWidth = PDRectangle.A4.getWidth();
        float pageHeight = PDRectangle.A4.getHeight();

        // Cargar imagen de encabezado
        byte[] headerBytes = new ClassPathResource("header.png").getInputStream().readAllBytes();
        PDImageXObject headerImage = PDImageXObject.createFromByteArray(documento, headerBytes, "header");

        // Calcular dimensiones de imagen (ancho completo, altura proporcional)
        float imageWidth = pageWidth;
        float imageHeight = headerImage.getHeight() * (pageWidth / headerImage.getWidth());

        // Contenido de la página
        PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

        // Dibujar encabezado (sin márgenes)
        contenido.drawImage(headerImage,
                0,                          // x: 0 (sin margen izquierdo)
                pageHeight - imageHeight,   // y: parte superior
                imageWidth,                 // ancho completo
                imageHeight                 // altura proporcional
        );

        // Posición inicial para contenido (debajo de la imagen)
        float y = pageHeight - imageHeight - 30;

        // Escribir username
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Usuario: " + orden.getUsername());
        contenido.endText();
        y -= lineHeight * 2;

        // Escribir el nombre
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Nombre: " + orden.getNombre());
        contenido.endText();
        y -= lineHeight * 2;

        // Escribir el apellido
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Apellido: " + orden.getApellido());
        contenido.endText();
        y -= lineHeight * 2;


        // Escribir el documento
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Documento: " + orden.getDocumento());
        contenido.endText();
        y -= lineHeight * 2;

        // Escribir la dirección
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Dirección: " + orden.getDireccion());
        contenido.endText();
        y -= lineHeight * 2;

        // Escribir la fecha
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("fecha: " + LocalDateTime.now());
        contenido.endText();
        y -= lineHeight * 2;

        // Título "Productos"
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText("Productos:");
        contenido.endText();
        y -= lineHeight;

        // Lista de productos con paginación
        for (productoCantidadDTO detalle : orden.getProductos()) {
            Optional<Productos> productoOpt = productoDao.findById(detalle.getProductoId());
            if (productoOpt.isPresent()) {
                Productos producto = productoOpt.get();
                String linea = String.format(
                        "- %s (x%d) - COP %.2f",
                        producto.getNombre(),
                        detalle.getCantidad(),
                        producto.getPrecio()
                );

                // Verificar espacio en página
                if (y < 100) {
                    contenido.close();
                    PDPage nuevaPagina = new PDPage(PDRectangle.A4);
                    documento.addPage(nuevaPagina);
                    contenido = new PDPageContentStream(documento, nuevaPagina);
                    y = pageHeight - 50; // Reiniciar posición en nueva página
                }

                contenido.beginText();
                contenido.setFont(PDType1Font.HELVETICA, 10);
                contenido.newLineAtOffset(marginLeft, y);
                contenido.showText(linea);
                contenido.endText();
                y -= lineHeight;
            }
        }

        // Total
        contenido.beginText();
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contenido.newLineAtOffset(marginLeft, y);
        contenido.showText(String.format("Total: COP %.2f", orden.getTotal()));
        contenido.endText();

        // Finalizar
        contenido.close();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        documento.save(out);
        documento.close();
        return out.toByteArray();
    }
}
