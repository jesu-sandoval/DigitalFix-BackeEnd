package com.digitalfix.catalog.repository;

import com.digitalfix.catalog.model.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {
    Optional<CatalogItem> findByCodigo(String codigo);
}