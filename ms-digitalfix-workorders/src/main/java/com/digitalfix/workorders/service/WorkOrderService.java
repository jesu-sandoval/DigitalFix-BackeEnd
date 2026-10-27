package com.digitalfix.workorders.service;

import com.digitalfix.workorders.model.WorkOrder;
import com.digitalfix.workorders.model.WorkOrderStatus;
import com.digitalfix.workorders.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkOrderService {

    private final WorkOrderRepository repositorio;
    private final OrderNotificationPublisher notificationPublisher;

    public WorkOrderService(WorkOrderRepository repositorio, OrderNotificationPublisher notificationPublisher) {
        this.repositorio = repositorio;
        this.notificationPublisher = notificationPublisher;
    }
    public List<WorkOrder> listarTodas() {
        return repositorio.findAll();
    }

    public WorkOrder buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada con ID: " + id));
    }


    public WorkOrder crear(WorkOrder orden) {
        orden.setEstado(WorkOrderStatus.CREADA);
        return repositorio.save(orden);
    }


    public WorkOrder actualizarEstado(Long id, WorkOrderStatus nuevoEstado, String tecnico) {
        WorkOrder orden = buscarPorId(id);

        if (nuevoEstado == WorkOrderStatus.EN_EJECUCION && orden.getEstado() == WorkOrderStatus.CREADA) {
            throw new RuntimeException("Regla de negocio violada: No se puede pasar");
        }
        if (nuevoEstado == WorkOrderStatus.ASIGNADA && tecnico != null && !tecnico.isBlank()) {
            orden.setTecnicoAsignado(tecnico);
        }

        orden.setEstado(nuevoEstado);
        WorkOrder ordenGuardada = repositorio.save(orden);

     //disparar eventos asincronos a rabbitmq
        try {
            String emailDestino = ordenGuardada.getClienteEmail() != null && !ordenGuardada.getClienteEmail().isBlank()
                    ? ordenGuardada.getClienteEmail()
                    : (ordenGuardada.getClienteId() != null && ordenGuardada.getClienteId().contains("@")
                        ? ordenGuardada.getClienteId()
                        : "cliente@digitalfix.cl");

            String nombreEstado = nuevoEstado != null ? nuevoEstado.name() : "ACTUALIZADA";

// e lenotifica siempre al cliente del cambio de estado
            notificationPublisher.notificarClientePorEmail(
                    ordenGuardada.getId(),
                    emailDestino,
                    ordenGuardada.getClienteNombre(),
                    nombreEstado,
                    "Su orden ha cambiado de estado a: " + nombreEstado
            );

 // si la orden fue asignada a un tecnico q genera ticket 
            if (WorkOrderStatus.ASIGNADA.equals(nuevoEstado)) {
                notificationPublisher.despacharTecnicoTerreno(
                        ordenGuardada.getId(),
                        tecnico != null ? tecnico : ordenGuardada.getTecnicoAsignado(),
                        "Cuadrilla Central",
                        ordenGuardada.getDireccion(),
                        ordenGuardada.getDescripcion()
                );
            }
        } catch (Exception e) {
            System.err.println("Advertencia: No se pudo enviar evento a RabbitMQ: " + e.getMessage());
        }

        return ordenGuardada;
    }
}