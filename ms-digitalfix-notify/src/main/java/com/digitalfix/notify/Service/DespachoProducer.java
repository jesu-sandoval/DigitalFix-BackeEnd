package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.DespachoMensaje;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;



@Service
public class DespachoProducer {

    private final RabbitTemplate rabbitTemplate;

    public DespachoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    
//envia el mensaje usando la ruta de despacho
    public void enviarTicketDespacho(DespachoMensaje despacho) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME, 
                "dispatch.ticket", 
                despacho
        );
        System.out.println("Ticket de terreno despachado para la orden #" + despacho.getOrdenId());
    }
}

