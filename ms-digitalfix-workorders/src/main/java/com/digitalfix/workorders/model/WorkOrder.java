package com.digitalfix.workorders.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cliente_id", nullable = false, length = 100)
    private String clienteId;


    @Column(name = "cliente_nombre", length = 150)
    private String clienteNombre;

    @Column(name = "cliente_email", length = 150)
    private String clienteEmail;
    @Column(name = "tecnico_asignado", length = 150)
    private String tecnicoAsignado;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    @Column(nullable = false, length = 200)
    private String direccion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkOrderStatus estado = WorkOrderStatus.CREADA;
    @Column(precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    @PrePersist
    public void antesDeGuardar() {
        if (this.estado == null) {
            this.estado = WorkOrderStatus.CREADA;
        }
        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    public void antesDeActualizar() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}