package com.softdevoluciones.repository;

import com.softdevoluciones.entity.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DevolucionRepository
        extends JpaRepository<Devolucion, Long>,
                JpaSpecificationExecutor<Devolucion> {

    List<Devolucion> findByUsuarioIdOrderByFechaSolicitudDesc(Long usuarioId);

    Optional<Devolucion> findByIdAndUsuarioId(Long id, Long usuarioId);
}