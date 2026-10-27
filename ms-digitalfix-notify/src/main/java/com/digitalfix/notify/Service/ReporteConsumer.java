package com.digitalfix.notify.Service;

import com.digitalfix.notify.DTO.ReporteMensaje;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReporteConsumer {

    // Conjunto en memoria para garantizar idempotencia
    private final Set<String> eventosProcesados = ConcurrentHashMap.newKeySet();

    @RabbitListener(queues = "${app.rabbitmq.queue:q.cmd.report}")
    public void recibirReporte(ReporteMensaje reporte,
                               Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        System.out.println("\n[CONSUMIDOR] >>> Mensaje recibido para orden #" + reporte.getOrderId());
        System.out.println(
                "[CONSUMIDOR] EventId: " + reporte.getEventId() + " | Cliente: " + reporte.getClienteNombre());

        try {
            //Control de Idempotencia
            if (reporte.getEventId() != null && eventosProcesados.contains(reporte.getEventId())) {
                System.out.println(
                        "[CONSUMIDOR] Evento duplicado detectado (" + reporte.getEventId() + "). Descartando...");
                // Se confirma con ACK para sacarlo de la cola porque ya fue procesado
                channel.basicAck(tag, false);
                return;
            }

            // Validacion de datos 
            if (reporte.getOrderId() == null || reporte.getOrderId() <= 0) {
                throw new IllegalArgumentException("El ID de la orden es invalido o viene vacio");
            }

            //Simulacion de generacion del informe tecnico PDF
            System.out.println("[CONSUMIDOR] Generando informe PDF.");
            Thread.sleep(2000);
            System.out.println("[CONSUMIDOR] PDF generado con exito para la orden #" + reporte.getOrderId()
                    + " por un total de $" + reporte.getTotal());

            // Confirmacion exitosa 
            channel.basicAck(tag, false);
            if (reporte.getEventId() != null) {
                eventosProcesados.add(reporte.getEventId());
            }
            System.out.println("[CONSUMIDOR] ACK enviado a RabbitMQ. Tarea finalizada.\n");

        } catch (IllegalArgumentException e) {
            System.err.println("[CONSUMIDOR] Error de datos no recuperable: " + e.getMessage());
            System.err.println("[CONSUMIDOR] Enviando NACK (requeue=false) -> El mensaje viaja a q.cmd.report.dlq\n");
            channel.basicNack(tag, false, false);

        } catch (Exception e) {
            System.err.println("[CONSUMIDOR] Error inesperado procesando reporte: " + e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
