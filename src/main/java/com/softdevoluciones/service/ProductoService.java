package com.softdevoluciones.service;

import com.softdevoluciones.entity.Producto;
import com.softdevoluciones.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Producto no encontrado con id: " + id
                        )
                );
    }

    public List<Producto> listarActivos() {
        return productoRepository.findAll()
                .stream()
                .filter(Producto::getActivo)
                .toList();
    }
}