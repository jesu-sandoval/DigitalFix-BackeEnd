package com.digitalfix.workorders.controller;

import com.digitalfix.workorders.dto.StatusUpdateRequest;
import com.digitalfix.workorders.model.WorkOrder;
import com.digitalfix.workorders.service.OrderNotificationPublisher;
import com.digitalfix.workorders.service.WorkOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    private final WorkOrderService servicio;
    private final OrderNotificationPublisher notificationPublisher;

    public WorkOrderController(WorkOrderService servicio, OrderNotificationPublisher notificationPublisher) {
        this.servicio = servicio;
        this.notificationPublisher = notificationPublisher;
    }
    @GetMapping
    public ResponseEntity<List<WorkOrder>> listar() {
        return ResponseEntity.ok(servicio.listarTodas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<WorkOrder> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<WorkOrder> crear(@RequestBody WorkOrder orden) {
        return new ResponseEntity<>(servicio.crear(orden), HttpStatus.CREATED);
    }




    @PutMapping("/{id}/status")
    public ResponseEntity<WorkOrder> actualizarEstado(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(servicio.actualizarEstado(id, request.getStatus(), request.getTecnico()));
    }

//endpoint para la generacion del pdf 
    @PostMapping("/{id}/reporte")
    public ResponseEntity<Map<String, String>> generarReporteOrden(@PathVariable Long id) {
        WorkOrder orden = servicio.buscarPorId(id);

        notificationPublisher.solicitarReporteOrden(
                orden.getId(),
                orden.getClienteNombre(),
                orden.getTecnicoAsignado(),
                orden.getDescripcion(),
                orden.getTotal()
        );

        return ResponseEntity.ok(Map.of("mensaje", "Solicitud de reporte enviada a la cola RabbitMQ"));
    }
}