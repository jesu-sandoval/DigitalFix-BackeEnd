package com.digitalfix.notify.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;


public class ReporteMensaje implements Serializable {

    private String eventId;
    private String traceId;
    private String tipo;
    private LocalDateTime fecha;

    private Long orderId;
    private String clienteNombre;
    private String tecnicoAsignado;
    private String descripcion;
    private Double total;

    public ReporteMensaje() {
    }

    public ReporteMensaje(String eventId, String traceId, Long orderId, String clienteNombre, String tecnicoAsignado, String descripcion, Double total) {
        this.eventId = eventId;
        this.traceId = traceId;
        this.tipo = "GENERAR_REPORTE_PDF";
        this.fecha = LocalDateTime.now();
        this.orderId = orderId;
        this.clienteNombre = clienteNombre;
        this.tecnicoAsignado = tecnicoAsignado;
        this.descripcion = descripcion;
        this.total = total;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getTecnicoAsignado() { return tecnicoAsignado; }
    public void setTecnicoAsignado(String tecnicoAsignado) { this.tecnicoAsignado = tecnicoAsignado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
