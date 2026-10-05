package com.softdevoluciones.controller;

import com.softdevoluciones.dto.DevolucionResponse;
import com.softdevoluciones.dto.EstadoDevolucionRequest;
import com.softdevoluciones.entity.EstadoDevolucion;
import com.softdevoluciones.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/devoluciones")
public class AdminDevolucionController {

    private final DevolucionService devolucionService;

    public AdminDevolucionController(
            DevolucionService devolucionService
    ) {
        this.devolucionService = devolucionService;
    }

    @GetMapping
    public ResponseEntity<Page<DevolucionResponse>> listar(
            @RequestParam(required = false)
            EstadoDevolucion estado,

            @RequestParam(required = false)
            String motivo,

            @RequestParam(required = false)
            LocalDateTime fechaInicio,

            @RequestParam(required = false)
            LocalDateTime fechaFin,

            Pageable pageable
    ) {
        return ResponseEntity.ok(
                devolucionService.filtrarAdmin(
                        estado,
                        motivo,
                        fechaInicio,
                        fechaFin,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevolucionResponse> obtener(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                devolucionService.obtenerPorId(id)
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<DevolucionResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoDevolucionRequest request
    ) {
        return ResponseEntity.ok(
                devolucionService.cambiarEstado(
                        id,
                        request
                )
        );
    }
}