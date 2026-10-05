package com.softdevoluciones.dto;

import com.softdevoluciones.entity.EstadoDevolucion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DevolucionResponse {

    private Long id;
    private LocalDateTime fechaSolicitud;
    private EstadoDevolucion estado;
    private String motivo;
    private String comentario;
    private String observacionOperador;
    private Double importe;
    private Long compraId;
    private Long usuarioId;
    private List<DetalleDevolucionResponse> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoDevolucion getEstado() {
        return estado;
    }

    public void setEstado(EstadoDevolucion estado) {
        this.estado = estado;
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

    public String getObservacionOperador() {
        return observacionOperador;
    }

    public void setObservacionOperador(String observacionOperador) {
        this.observacionOperador = observacionOperador;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DetalleDevolucionResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucionResponse> detalles) {
        this.detalles = detalles;
    }

    public static class DetalleDevolucionResponse {

        private Long id;
        private Long detalleCompraId;
        private Long productoId;
        private String productoNombre;
        private String imagenUrl;
        private Integer cantidad;
        private Double importe;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getDetalleCompraId() {
            return detalleCompraId;
        }

        public void setDetalleCompraId(Long detalleCompraId) {
            this.detalleCompraId = detalleCompraId;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public String getProductoNombre() {
            return productoNombre;
        }

        public void setProductoNombre(String productoNombre) {
            this.productoNombre = productoNombre;
        }

        public String getImagenUrl() {
            return imagenUrl;
        }

        public void setImagenUrl(String imagenUrl) {
            this.imagenUrl = imagenUrl;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Double getImporte() {
            return importe;
        }

        public void setImporte(Double importe) {
            this.importe = importe;
        }
    }
}