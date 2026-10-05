package com.softdevoluciones.controller;

import com.softdevoluciones.dto.UsuarioResponse;
import com.softdevoluciones.entity.Usuario;
import com.softdevoluciones.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {

        List<UsuarioResponse> usuarios =
                usuarioService.listarTodos()
                        .stream()
                        .map(this::convertir)
                        .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                convertir(usuarioService.obtenerPorId(id))
        );
    }

    private UsuarioResponse convertir(
            Usuario usuario
    ) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }
}