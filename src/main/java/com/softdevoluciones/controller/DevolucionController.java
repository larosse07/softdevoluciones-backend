package com.softdevoluciones.controller;

import com.softdevoluciones.dto.DevolucionRequest;
import com.softdevoluciones.dto.DevolucionResponse;
import com.softdevoluciones.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devoluciones")
public class DevolucionController {

    private final DevolucionService devolucionService;

    public DevolucionController(
            DevolucionService devolucionService
    ) {
        this.devolucionService = devolucionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DevolucionResponse> crear(
            @Valid @RequestBody DevolucionRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        devolucionService.crearSolicitud(
                                request,
                                authentication.getName()
                        )
                );
    }

    @GetMapping("/mis-devoluciones")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<DevolucionResponse>> misDevoluciones(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                devolucionService.listarMisDevoluciones(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<DevolucionResponse> obtenerMiDevolucion(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                devolucionService.obtenerMiDevolucion(
                        id,
                        authentication.getName()
                )
        );
    }
}