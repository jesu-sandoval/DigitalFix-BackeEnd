package com.digitalfix.catalog.service;

import com.digitalfix.catalog.model.CatalogItem;
import com.digitalfix.catalog.repository.CatalogItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogService {

    private final CatalogItemRepository repositorio;

    public CatalogService(CatalogItemRepository repositorio) {
        this.repositorio = repositorio;
    }
    public List<CatalogItem> listarTodos() {
        return repositorio.findAll();
    }
    public CatalogItem buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("El item con ID " + id + " no existe"));
    }
    public CatalogItem guardar(CatalogItem item) {
        return repositorio.save(item);
    }



    public CatalogItem actualizar(Long id, CatalogItem datos) {
        CatalogItem existente = buscarPorId(id);


        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setDescripcion(datos.getDescripcion());
        existente.setActivo(datos.getActivo());



        return repositorio.save(existente);
    }
}