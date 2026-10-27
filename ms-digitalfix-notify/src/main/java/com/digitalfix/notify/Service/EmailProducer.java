package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.EmailMensaje;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void enviarNotificacionEmail(EmailMensaje email) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME, 
                "email.notificacion", 
                email
        );
        System.out.println("Email enviado exitosamente a la cola para orden #" + email.getOrdenId());
    }
}

