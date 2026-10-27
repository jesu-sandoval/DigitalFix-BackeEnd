package com.digitalfix.notify.Controller;

import com.digitalfix.notify.DTO.EmailMensaje;
import com.digitalfix.notify.Service.EmailProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notify/email")
public class EmailController {

    private final EmailProducer emailProducer;

    public EmailController(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    @PostMapping("/send")
    public ResponseEntity<String> enviarEmailPrueba(@RequestBody EmailMensaje email) {
        emailProducer.enviarNotificacionEmail(email);
        return ResponseEntity.ok("Mensaje de correo enviado correctamente.");
    }
}

