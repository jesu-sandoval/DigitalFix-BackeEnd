package com.digitalfix.notify.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;


public class EmailMensaje implements Serializable {

    private Long ordenId;
    private String clienteEmail;
    private String clienteNombre;
    private String estado;
    private String mensaje;
    private String fecha;

    public EmailMensaje() {
        this.fecha = LocalDateTime.now().toString();
    }

    public EmailMensaje(Long ordenId, String clienteEmail, String clienteNombre, String estado, String mensaje) {
        this.ordenId = ordenId;
        this.clienteEmail = clienteEmail;
        this.clienteNombre = clienteNombre;
        this.estado = estado;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now().toString();
    }

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }


    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}

