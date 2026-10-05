package com.softdevoluciones.service;

import com.softdevoluciones.dto.AuthResponse;
import com.softdevoluciones.dto.LoginRequest;
import com.softdevoluciones.dto.RegistroUsuarioRequest;
import com.softdevoluciones.entity.Rol;
import com.softdevoluciones.entity.Usuario;
import com.softdevoluciones.repository.UsuarioRepository;
import com.softdevoluciones.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse registrar(RegistroUsuarioRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese email"
            );
        }

        Usuario usuario = new Usuario(
        null,
        request.getNombre().trim(),
        email,
        passwordEncoder.encode(request.getPassword()),
        Rol.CLIENTE
);

        usuario = usuarioRepository.save(usuario);

        return crearRespuesta(usuario, null);
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                request.getPassword()
                        )
                );

        String token = jwtTokenProvider.generateToken(authentication);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        return crearRespuesta(usuario, token);
    }

    private AuthResponse crearRespuesta(
            Usuario usuario,
            String token
    ) {
        return new AuthResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name()
        );
    }
}