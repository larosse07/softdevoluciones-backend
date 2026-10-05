package com.softdevoluciones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

public class DevolucionRequest {

    @NotNull(message = "La compra es obligatoria")
    private Long compraId;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String comentario;

    @NotEmpty(message = "Debe incluir al menos un producto para devolver")
    @Valid
    private List<DetalleDevolucionRequest> detalles = new ArrayList<>();

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<DetalleDevolucionRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucionRequest> detalles) {
        this.detalles = detalles;
    }

    public static class DetalleDevolucionRequest {

        @NotNull(message = "El detalle de compra es obligatorio")
        private Long detalleCompraId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor que cero")
        private Integer cantidad;

        public Long getDetalleCompraId() {
            return detalleCompraId;
        }

        public void setDetalleCompraId(Long detalleCompraId) {
            this.detalleCompraId = detalleCompraId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}