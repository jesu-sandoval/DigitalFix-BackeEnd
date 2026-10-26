package com.digitalfix.notify.Controller;

import com.digitalfix.notify.DTO.ReporteMensaje;
import com.digitalfix.notify.Service.ReporteProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteProducer reporteProducer;

    // Comprobacion de salud del servicio 
    @GetMapping("/salud")
    public ResponseEntity<Map<String, String>> salud() {
        return new ResponseEntity<>(
                Map.of("estado", "Servicio ms-digitalfix-notify operativo", "flujo", "q.cmd.report"),
                HttpStatus.OK
        );
    }

    // Recibe la orden para generar reporte, la encola y responde de inmediato.
    @PostMapping
    public ResponseEntity<Map<String, Object>> solicitarReporte(@RequestBody ReporteMensaje reporte) {

        if (reporte.getEventId() == null || reporte.getEventId().isEmpty()) {
            reporte.setEventId(UUID.randomUUID().toString());
        }
        if (reporte.getTraceId() == null || reporte.getTraceId().isEmpty()) {
            reporte.setTraceId(UUID.randomUUID().toString());
        }
        reporte.setFecha(LocalDateTime.now());
        if (reporte.getTipo() == null) {
            reporte.setTipo("GENERAR_REPORTE_PDF");
        }
        reporteProducer.publicarReporte(reporte);

        return new ResponseEntity<>(
                Map.of(
                        "mensaje", "Solicitud de reporte encolada con exito",
                        "eventId", reporte.getEventId(),
                        "orderId", reporte.getOrderId() != null ? reporte.getOrderId() : 0,
                        "estado", "EN_COLA"
                ),
                HttpStatus.ACCEPTED
        );
    }
}
