package com.softdevoluciones.controller;

import com.softdevoluciones.dto.CompraResponse;
import com.softdevoluciones.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @GetMapping("/mis-compras")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CompraResponse>> misCompras(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                compraService.listarMisCompras(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CompraResponse> obtenerMiCompra(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                compraService.obtenerMiCompra(
                        id,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/admin/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompraResponse>> todasLasCompras() {
        return ResponseEntity.ok(
                compraService.listarTodas()
        );
    }

    @PatchMapping("/admin/{id}/entregar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompraResponse> marcarComoEntregada(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                compraService.marcarComoEntregada(id)
        );
    }
}