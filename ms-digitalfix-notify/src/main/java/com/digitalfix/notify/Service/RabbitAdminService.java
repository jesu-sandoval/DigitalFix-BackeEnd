package com.digitalfix.notify.Service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    /** nueva cola en RabbitMQ.*/   
    public boolean crearCola(String nombreCola, boolean durable) {
        Queue cola = new Queue(nombreCola, durable);
        amqpAdmin.declareQueue(cola);
        System.out.println("[ADMIN] Cola creada en RabbitMQ: " + nombreCola + " (durable=" + durable + ")");
        return true;
    }

    /** Elimina una cola existente en RabbitMQ. */
    public boolean eliminarCola(String nombreCola) {
        boolean eliminada = amqpAdmin.deleteQueue(nombreCola);
        System.out.println("[ADMIN] Cola eliminada en RabbitMQ: " + nombreCola + " (resultado=" + eliminada + ")");
        return eliminada;
    }

    /** Obtiene informacion de una cola */
    public Properties obtenerInfoCola(String nombreCola) {
        return amqpAdmin.getQueueProperties(nombreCola);
    }
}
