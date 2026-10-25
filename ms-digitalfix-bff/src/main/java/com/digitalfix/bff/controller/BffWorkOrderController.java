package com.digitalfix.bff.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/workorders")
public class BffWorkOrderController {

    private final RestClient restClient;
    private final String workordersServiceUrl;

    public BffWorkOrderController(RestClient restClient, @Value("${services.workorders.url}") String workordersServiceUrl) {
        this.restClient = restClient;
        this.workordersServiceUrl = workordersServiceUrl;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('Cliente', 'Admin', 'Supervisor', 'Auditor')")
    public ResponseEntity<String> listarOrdenes() {
        String resultado = restClient.get()
                .uri(workordersServiceUrl + "/api/workorders")
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(resultado);
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
}