package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.DespachoMensaje;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DespachoConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_DISPATCH)
    public void recibirDespacho(DespachoMensaje despacho,
                                Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        System.out.println("--------------------------------------------------");
        System.out.println(">>>ticket<<<");
        System.out.println("Orden ID:   #" + despacho.getOrdenId());
        System.out.println("Técnico:    " + despacho.getTecnico());
        System.out.println("Cuadrilla:  " + despacho.getCuadrilla());
        System.out.println("Dirección:  " + despacho.getDireccion());
        System.out.println("Trabajo:    " + despacho.getDescripcion());
        System.out.println("Fecha/Hora: " + despacho.getFecha());
        System.out.println("Acción:     Ruta trazada y orden entregada a terreno.");
        System.out.println("--------------------------------------------------");

        try {
            if (despacho.getOrdenId() == null || despacho.getOrdenId() <= 0) {
                throw new IllegalArgumentException("Identificador de orden invalido");
            }
            Thread.sleep(1000);
            channel.basicAck(tag, false);
            System.out.println("despacho-Ticket confirmado-ACK-eliminado de la cola.\n");

        } catch (IllegalArgumentException e) {
            System.err.println("despacho-error: " + e.getMessage());
            channel.basicNack(tag, false, false);

        } catch (Exception e) {
            System.err.println("despacho-Error: " + e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
