package com.softdevoluciones.service;

import com.softdevoluciones.dto.CompraResponse;
import com.softdevoluciones.dto.DetalleCompraResponse;
import com.softdevoluciones.entity.Compra;
import com.softdevoluciones.entity.DetalleCompra;
import com.softdevoluciones.entity.Usuario;
import com.softdevoluciones.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<CompraResponse> listarMisCompras(String email) {

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        return compraRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompraResponse obtenerMiCompra(
            Long compraId,
            String email
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        Compra compra = compraRepository
                .findByIdAndUsuarioId(compraId, usuario.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Compra no encontrada o no pertenece al usuario"
                        )
                );

        return convertirAResponse(compra);
    }

    @Transactional(readOnly = true)
    public List<CompraResponse> listarTodas() {

        return compraRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public CompraResponse marcarComoEntregada(Long compraId) {

        if (compraId == null) {
            throw new IllegalArgumentException(
                    "El ID de la compra es obligatorio"
            );
        }

        Compra compra = compraRepository
                .findById(compraId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Compra no encontrada"
                        )
                );

        if (!"EN_TRANSITO".equals(compra.getEstado())) {
            throw new IllegalStateException(
                    "Solo se puede entregar una compra que está EN_TRANSITO"
            );
        }

        compra.setEstado("ENTREGADO");

        Compra compraActualizada =
                compraRepository.save(compra);

        return convertirAResponse(compraActualizada);
    }

    private CompraResponse convertirAResponse(Compra compra) {

        CompraResponse response = new CompraResponse();

        response.setId(compra.getId());
        response.setFecha(compra.getFecha());
        response.setTotal(compra.getTotal());
        response.setEstado(compra.getEstado());

        if (compra.getUsuario() != null) {

            response.setUsuarioId(
                    compra.getUsuario().getId()
            );

            response.setNombreCliente(
                    compra.getUsuario().getNombre()
            );
        }

        List<DetalleCompraResponse> detalles =
                compra.getDetalles()
                        .stream()
                        .map(this::convertirDetalle)
                        .toList();

        response.setDetalles(detalles);

        return response;
    }

    private DetalleCompraResponse convertirDetalle(
            DetalleCompra detalle
    ) {

        DetalleCompraResponse response =
                new DetalleCompraResponse();

        response.setId(detalle.getId());
        response.setCantidad(detalle.getCantidad());
        response.setPrecioUnitario(
                detalle.getPrecioUnitario()
        );

        if (detalle.getProducto() != null) {

            response.setProductoId(
                    detalle.getProducto().getId()
            );

            response.setProductoNombre(
                    detalle.getProducto().getNombre()
            );

            response.setProductoDescripcion(
                    detalle.getProducto().getDescripcion()
            );

            response.setImagenUrl(
                    detalle.getProducto().getImagenUrl()
            );
        }

        return response;
    }
}