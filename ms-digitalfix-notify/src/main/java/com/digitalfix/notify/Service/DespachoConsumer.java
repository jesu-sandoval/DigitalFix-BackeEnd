package com.digitalfix.notify.Service;

import com.digitalfix.notify.Config.RabbitConfig;
import com.digitalfix.notify.DTO.DespachoMensaje;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class DespachoConsumer {

//se escucha la cola en segundo plano 
//txt que va a sali en consola
    @RabbitListener(queues = RabbitConfig.QUEUE_DISPATCH)
    public void recibirDespacho(DespachoMensaje despacho) {
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
    }
}

