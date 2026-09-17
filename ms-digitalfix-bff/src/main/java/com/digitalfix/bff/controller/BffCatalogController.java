package com.digitalfix.bff.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/catalog/services")
public class BffCatalogController {

    private final RestClient restClient;
    private final String catalogServiceUrl;

    public BffCatalogController(RestClient restClient, @Value("${services.catalog.url}") String catalogServiceUrl) {
        this.restClient = restClient;
        this.catalogServiceUrl = catalogServiceUrl;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Supervisor')")
    public ResponseEntity<String> listarServicios() {
        String resultado = restClient.get()
                .uri(catalogServiceUrl + "/api/catalog/services")
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(resultado);
    }
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> crearServicio(@RequestBody String body) {
        String resultado = restClient.post()
                .uri(catalogServiceUrl + "/api/catalog/services")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        return ResponseEntity.status(201).body(resultado);
    }




    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> actualizarServicio(@PathVariable Long id, @RequestBody String body) {
        String resultado = restClient.put()
                .uri(catalogServiceUrl + "/api/catalog/services/" + id)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(resultado);
    }
}