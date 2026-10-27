package com.digitalfix.workorders.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Service
public class OrderNotificationPublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "cmd.topic";


    
    public OrderNotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

//  envia aviso de correo hacia la cola de emails
    public void notificarClientePorEmail(Long ordenId, String clienteEmail, String clienteNombre, String estado, String detalle) {
        Map<String, Object> mensajeEmail = new HashMap<>();
        mensajeEmail.put("ordenId", ordenId);
        mensajeEmail.put("clienteEmail", clienteEmail != null ? clienteEmail : "cliente@digitalfix.cl");
        mensajeEmail.put("clienteNombre", clienteNombre != null ? clienteNombre : "Cliente");
        mensajeEmail.put("estado", estado);
        mensajeEmail.put("mensaje", detalle);

    //publica usando la clave de ruta para correos
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "email.notificacion", mensajeEmail);
        System.out.println(">>> Evento de correo publicado para orden #" + ordenId);
    }

//seenvia ticket de traslado hacia la cola de despacho
    public void despacharTecnicoTerreno(Long ordenId, String tecnico, String cuadrilla, String direccion, String descripcion) {
        Map<String, Object> mensajeDespacho = new HashMap<>();
        mensajeDespacho.put("ordenId", ordenId);
        mensajeDespacho.put("tecnico", tecnico);
        mensajeDespacho.put("cuadrilla", cuadrilla != null ? cuadrilla : "Cuadrilla Asignada");
        mensajeDespacho.put("direccion", direccion != null ? direccion : "Direccion del cliente");
        mensajeDespacho.put("descripcion", descripcion != null ? descripcion : "Trabajo de mantencion");

     //lo publica usando la ruta para despacho
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "dispatch.ticket", mensajeDespacho);
        System.out.println(">>> Evento de despacho publicado para orden #" + ordenId);
    }

//emite la solicitud de reporte 
    public void solicitarReporteOrden(Long orderId, String clienteNombre, String tecnicoAsignado, String descripcion, Double total) {
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("eventId", java.util.UUID.randomUUID().toString());
        reporte.put("traceId", java.util.UUID.randomUUID().toString());
        reporte.put("tipo", "GENERAR_REPORTE_PDF");
        reporte.put("orderId", orderId);
        reporte.put("clienteNombre", clienteNombre != null ? clienteNombre : "Cliente DigitalFix");
        reporte.put("tecnicoAsignado", tecnicoAsignado != null ? tecnicoAsignado : "Sin asignar");
        reporte.put("descripcion", descripcion != null ? descripcion : "Servicio técnico");
        reporte.put("total", total != null ? total : 0.0);

 //se despacha a traves del exchange configurado hacia la cola de reportes
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "report.gen", reporte);
        System.out.println(">>> Solicitud de PDF emitida a RabbitMQ para la orden #" + orderId);
    }

    public void solicitarReporteOrden(Long orderId, String clienteNombre, String tecnicoAsignado, String descripcion, java.math.BigDecimal total) {
        solicitarReporteOrden(orderId, clienteNombre, tecnicoAsignado, descripcion, total != null ? total.doubleValue() : 0.0);
    }
}

