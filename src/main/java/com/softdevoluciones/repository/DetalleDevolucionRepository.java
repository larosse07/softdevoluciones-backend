package com.softdevoluciones.repository;

import com.softdevoluciones.entity.DetalleDevolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucion, Long> {

    @Query("""
        SELECT COALESCE(SUM(dd.cantidad), 0)
        FROM DetalleDevolucion dd
        JOIN dd.devolucion d
        WHERE dd.detalleCompra.id = :detalleCompraId
          AND d.estado IN (
              com.softdevoluciones.entity.EstadoDevolucion.SOLICITADA,
              com.softdevoluciones.entity.EstadoDevolucion.EN_REVISION,
              com.softdevoluciones.entity.EstadoDevolucion.APROBADA,
              com.softdevoluciones.entity.EstadoDevolucion.COMPLETADA
          )
        """)
    Long sumarCantidadesDevueltasPorDetalle(
            @Param("detalleCompraId") Long detalleCompraId
    );
}