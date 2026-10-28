package com.digitalfix.bff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping({"/api/workorders", "/workorders"})
public class BffWorkOrderController {

    private final RestClient restClient;
    private final String workordersServiceUrl;
    private final String notifyServiceUrl;

    public BffWorkOrderController(
            RestClient restClient,
            @Value("${SERVICES_WORKORDERS_URL}") String workordersServiceUrl,
            @Value("${SERVICES_NOTIFY_URL:http://notify-service:8084}") String notifyServiceUrl) {
        this.restClient = restClient;
        this.workordersServiceUrl = workordersServiceUrl;
        this.notifyServiceUrl = notifyServiceUrl;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Cliente', 'Admin', 'Supervisor', 'Auditor')")
    public ResponseEntity<String> listarOrdenes() {
        try {
            String resultado = restClient.get()
                    .uri(workordersServiceUrl + "/api/workorders")
                    .retrieve()
                    .body(String.class);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.ok("[]");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('Cliente', 'Admin', 'Supervisor')")
    public ResponseEntity<String> crearOrden(@RequestBody String body) {
        String resultado = restClient.post()
                .uri(workordersServiceUrl + "/api/workorders")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        return ResponseEntity.status(201).body(resultado);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('Cliente', 'Supervisor', 'Admin')")
    public ResponseEntity<String> actualizarEstado(@PathVariable Long id, @RequestBody String body) {
        String resultado = restClient.put()
                .uri(workordersServiceUrl + "/api/workorders/" + id + "/status")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(resultado);
    }

    // Dispara el evento asincrono hacia ms-digitalfix-notify
    @PostMapping("/{id}/report")
    @PreAuthorize("hasAnyRole('Cliente', 'Supervisor', 'Admin')")
    public ResponseEntity<String> solicitarReporte(@PathVariable Long id, @RequestBody(required = false) String body) {
        try {
            String payload = (body != null && !body.isBlank()) ? body : "{\"orderId\":" + id + "}";
            String resultado = restClient.post()
                    .uri(notifyServiceUrl + "/reportes")
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .retrieve()
                    .body(String.class);
            return ResponseEntity.status(202).body(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Falla al comunicar con el servicio de notificaciones: " + e.getMessage() + "\"}");
        }
    }
}
