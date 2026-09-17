package com.digitalfix.workorders.service;

import com.digitalfix.workorders.model.WorkOrder;
import com.digitalfix.workorders.model.WorkOrderStatus;
import com.digitalfix.workorders.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkOrderService {

    private final WorkOrderRepository repositorio;

    public WorkOrderService(WorkOrderRepository repositorio) {
        this.repositorio = repositorio;
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
        return repositorio.save(orden);
    }
}