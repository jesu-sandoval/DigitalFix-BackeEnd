package com.digitalfix.notify.Controller;

import com.digitalfix.notify.DTO.DespachoMensaje;
import com.digitalfix.notify.Service.DespachoProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/notify/dispatch")
public class DespachoController {

    private final DespachoProducer despachoProducer;

    public DespachoController(DespachoProducer despachoProducer) {
        this.despachoProducer = despachoProducer;
    }



    @PostMapping("/send")
    public ResponseEntity<String> enviarDespachoPrueba(@RequestBody DespachoMensaje despacho) {
        despachoProducer.enviarTicketDespacho(despacho);
        return ResponseEntity.ok("Ticket de despacho enviada");
    }
}

