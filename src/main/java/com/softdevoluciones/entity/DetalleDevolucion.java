package com.softdevoluciones.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_devoluciones")
public class DetalleDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double importe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devolucion_id", nullable = false)
    private Devolucion devolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_compra_id", nullable = false)
    private DetalleCompra detalleCompra;

    public DetalleDevolucion() {
    }

    public DetalleDevolucion(
            Long id,
            Integer cantidad,
            Double importe,
            Devolucion devolucion,
            DetalleCompra detalleCompra
    ) {
        this.id = id;
        this.cantidad = cantidad;
        this.importe = importe;
        this.devolucion = devolucion;
        this.detalleCompra = detalleCompra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
    }

    public DetalleCompra getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(DetalleCompra detalleCompra) {
        this.detalleCompra = detalleCompra;
    }
}