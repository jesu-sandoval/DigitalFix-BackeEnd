package com.digitalfix.notify.Service;

import com.digitalfix.notify.DTO.ReporteMensaje;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReporteProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange:cmd.direct}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key:report.gen}")
    private String routingKey;

    public void publicarReporte(ReporteMensaje reporte) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, reporte);
        System.out.println("[PRODUCER] Solicitud de reporte enviada para orden #" + reporte.getOrderId()
                + " | EventId: " + reporte.getEventId()
                + " | RoutingKey: " + routingKey);
    }
}
