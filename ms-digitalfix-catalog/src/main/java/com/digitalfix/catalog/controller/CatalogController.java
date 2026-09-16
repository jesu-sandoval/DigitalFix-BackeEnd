package com.digitalfix.catalog.controller;


import com.digitalfix.catalog.model.CatalogItem;
import com.digitalfix.catalog.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/catalog/services")
public class CatalogController {

    private final CatalogService servicio;

    public CatalogController(CatalogService servicio) {
        this.servicio = servicio;
    }
    @GetMapping
    public ResponseEntity<List<CatalogItem>> listar() {
        return ResponseEntity.ok(servicio.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogItem> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.buscarPorId(id));
    }



    @PostMapping
    public ResponseEntity<CatalogItem> crear(@RequestBody CatalogItem item) {
        return new ResponseEntity<>(servicio.guardar(item), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CatalogItem> actualizar(@PathVariable Long id, @RequestBody CatalogItem item) {
        return ResponseEntity.ok(servicio.actualizar(id, item));
    }
}