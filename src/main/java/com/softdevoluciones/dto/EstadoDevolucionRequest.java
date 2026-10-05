package com.softdevoluciones.dto;

import com.softdevoluciones.entity.EstadoDevolucion;
import jakarta.validation.constraints.NotNull;

public class EstadoDevolucionRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoDevolucion estado;

    private String observacion;

    public EstadoDevolucionRequest() {
    }

    public EstadoDevolucionRequest(
            EstadoDevolucion estado,
            String observacion
    ) {
        this.estado = estado;
        this.observacion = observacion;
    }

    public EstadoDevolucion getEstado() {
        return estado;
    }

    public void setEstado(EstadoDevolucion estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}