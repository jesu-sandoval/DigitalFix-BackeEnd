package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.EmailMensaje;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class EmailConsumer {

//esto escucha la cola en segundo plano y en este caso lo imprime en consola
    @RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
    public void recibirEmail(EmailMensaje email) {
        System.out.println("==================================================");
        System.out.println(">>>correo<<<");
        System.out.println("Destinatario: " + email.getClienteEmail() + " (" + email.getClienteNombre() + ")");
        System.out.println("Orden ID:     #" + email.getOrdenId());
        System.out.println("Estado:       " + email.getEstado());
        System.out.println("Cuerpo:       " + email.getMensaje());
        System.out.println("Fecha-Hora:   " + email.getFecha());
        System.out.println("==================================================");
    }
}

