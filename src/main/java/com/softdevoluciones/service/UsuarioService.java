package com.softdevoluciones.service;

import com.softdevoluciones.entity.Usuario;
import com.softdevoluciones.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(
                email.trim().toLowerCase()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Usuario no encontrado"
                )
        );
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado con id: " + id
                        )
                );
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}