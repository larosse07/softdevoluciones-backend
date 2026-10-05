package com.softdevoluciones.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraResponse {

    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private String estado;
    private Long usuarioId;
    private String nombreCliente;
    private List<DetalleCompraResponse> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<DetalleCompraResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraResponse> detalles) {
        this.detalles = detalles;
    }
}