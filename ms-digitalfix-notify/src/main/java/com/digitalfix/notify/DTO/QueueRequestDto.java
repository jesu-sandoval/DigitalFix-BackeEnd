package com.digitalfix.notify.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class QueueRequestDto {

    @NotBlank(message = "El nombre de la cola es obligatorio y no puede estar vacio")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de la cola solo puede contener letras, numeros, puntos, guiones y guion bajo")
    private String nombre;

    private boolean durable = true;

    public QueueRequestDto() {
    }

    public QueueRequestDto(String nombre, boolean durable) {
        this.nombre = nombre;
        this.durable = durable;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isDurable() { return durable; }
    public void setDurable(boolean durable) { this.durable = durable; }
}
