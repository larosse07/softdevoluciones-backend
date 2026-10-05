package com.softdevoluciones.controller;

import com.softdevoluciones.dto.AuthResponse;
import com.softdevoluciones.dto.LoginRequest;
import com.softdevoluciones.dto.RegistroUsuarioRequest;
import com.softdevoluciones.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(
            @Valid @RequestBody RegistroUsuarioRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registrar(request));
    }
}