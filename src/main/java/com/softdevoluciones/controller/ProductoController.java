package com.softdevoluciones.controller;

import com.softdevoluciones.dto.ProductoResponse;
import com.softdevoluciones.entity.Producto;
import com.softdevoluciones.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'OPERADOR', 'ADMIN')")
    public ResponseEntity<List<ProductoResponse>> listarActivos() {

        List<ProductoResponse> productos =
                productoService.listarActivos()
                        .stream()
                        .map(this::convertir)
                        .toList();

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'OPERADOR', 'ADMIN')")
    public ResponseEntity<ProductoResponse> obtener(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                convertir(productoService.obtenerPorId(id))
        );
    }

    private ProductoResponse convertir(
            Producto producto
    ) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getImagenUrl(),
                producto.getActivo()
        );
    }
}