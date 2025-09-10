package com.email.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String to, String subject, String html) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();

        // true indica que el contenido será HTML
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setFrom("jocatoshoes574@gmail.com"); // debe coincidir con spring.mail.username
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // segundo parámetro true para que interprete como HTML

        mailSender.send(mensaje);
    }


}
