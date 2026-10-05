package com.softdevoluciones.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDevolucion estado;

    @Column(nullable = false)
    private String motivo;

    private String comentario;

    private String observacionOperador;

    @Column(nullable = false)
    private Double importe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(
            mappedBy = "devolucion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleDevolucion> detalles = new ArrayList<>();

    public Devolucion() {
    }

    public Devolucion(
            Long id,
            LocalDateTime fechaSolicitud,
            EstadoDevolucion estado,
            String motivo,
            String comentario,
            String observacionOperador,
            Double importe,
            Compra compra,
            Usuario usuario,
            List<DetalleDevolucion> detalles
    ) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.motivo = motivo;
        this.comentario = comentario;
        this.observacionOperador = observacionOperador;
        this.importe = importe;
        this.compra = compra;
        this.usuario = usuario;
        this.detalles = detalles;
    }

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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleDevolucion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDevolucion> detalles) {
        this.detalles = detalles;
    }
}