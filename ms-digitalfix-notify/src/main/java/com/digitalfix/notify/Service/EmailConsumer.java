package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.EmailMensaje;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
    public void recibirEmail(EmailMensaje email,
                             Channel channel,
                             @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

        System.out.println("==================================================");
        System.out.println(">>>correo<<<");
        System.out.println("Destinatario: " + email.getClienteEmail() + " (" + email.getClienteNombre() + ")");
        System.out.println("Orden ID:     #" + email.getOrdenId());
        System.out.println("Estado:       " + email.getEstado());
        System.out.println("Cuerpo:       " + email.getMensaje());
        System.out.println("Fecha-Hora:   " + email.getFecha());
        System.out.println("==================================================");

        try {
            if (email.getClienteEmail() == null || email.getClienteEmail().isBlank()) {
                throw new IllegalArgumentException("El correo electronico del cliente es obligatorio");
            }
            Thread.sleep(1000);
            channel.basicAck(tag, false);
            System.out.println("email consumer-Mensaje confirmado-ACK");

        } catch (IllegalArgumentException e) {
            System.err.println("email consumer-Datos invalidos: " + e.getMessage());
            channel.basicNack(tag, false, false);

        } catch (Exception e) {
            System.err.println("email consumer-Error inesperado: " + e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
