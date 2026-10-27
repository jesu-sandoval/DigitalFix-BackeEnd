package com.digitalfix.notify.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;



public class DespachoMensaje implements Serializable {

    private Long ordenId;
    private String tecnico;
    private String cuadrilla;
    private String direccion;
    private String descripcion;
    private String fecha;

    public DespachoMensaje() {
        this.fecha = LocalDateTime.now().toString();
    }

    public DespachoMensaje(Long ordenId, String tecnico, String cuadrilla, String direccion, String descripcion) {
        this.ordenId = ordenId;
        this.tecnico = tecnico;
        this.cuadrilla = cuadrilla;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now().toString();
    }

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }

    public String getCuadrilla() { return cuadrilla; }
    public void setCuadrilla(String cuadrilla) { this.cuadrilla = cuadrilla; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}

