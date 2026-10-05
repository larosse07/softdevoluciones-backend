package com.softdevoluciones.service;

import com.softdevoluciones.dto.DevolucionRequest;
import com.softdevoluciones.dto.DevolucionResponse;
import com.softdevoluciones.dto.EstadoDevolucionRequest;
import com.softdevoluciones.entity.Compra;
import com.softdevoluciones.entity.DetalleCompra;
import com.softdevoluciones.entity.DetalleDevolucion;
import com.softdevoluciones.entity.Devolucion;
import com.softdevoluciones.entity.EstadoDevolucion;
import com.softdevoluciones.entity.Usuario;
import com.softdevoluciones.repository.CompraRepository;
import com.softdevoluciones.repository.DetalleCompraRepository;
import com.softdevoluciones.repository.DetalleDevolucionRepository;
import com.softdevoluciones.repository.DevolucionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final DetalleDevolucionRepository detalleDevolucionRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraRepository compraRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public DevolucionResponse crearSolicitud(
            DevolucionRequest request,
            String email
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        Compra compra = compraRepository
                .findByIdAndUsuarioId(
                        request.getCompraId(),
                        usuario.getId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La compra no existe o no pertenece al usuario"
                        )
                );

        if (request.getDetalles() == null
                || request.getDetalles().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe incluir al menos un producto"
            );
        }

        Map<Long, Integer> cantidadesSolicitadas =
                new HashMap<>();

        for (DevolucionRequest.DetalleDevolucionRequest item
                : request.getDetalles()) {

            if (item.getCantidad() == null
                    || item.getCantidad() <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero"
                );
            }

            cantidadesSolicitadas.merge(
                    item.getDetalleCompraId(),
                    item.getCantidad(),
                    Integer::sum
            );
        }

        Devolucion devolucion = new Devolucion(
                null,
                LocalDateTime.now(),
                EstadoDevolucion.SOLICITADA,
                request.getMotivo().trim(),
                request.getComentario(),
                null,
                0.0,
                compra,
                usuario,
                new ArrayList<>()
        );

        double importeTotal = 0.0;

        for (Map.Entry<Long, Integer> entry
                : cantidadesSolicitadas.entrySet()) {

            Long detalleCompraId = entry.getKey();
            Integer cantidadSolicitada = entry.getValue();

            DetalleCompra detalleCompra =
                    detalleCompraRepository.findById(detalleCompraId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Detalle de compra no encontrado: "
                                                    + detalleCompraId
                                    )
                            );

            if (!detalleCompra.getCompra().getId()
                    .equals(compra.getId())) {

                throw new IllegalArgumentException(
                        "El producto solicitado no pertenece a la compra indicada"
                );
            }

            Long cantidadDevuelta =
                    detalleDevolucionRepository
                            .sumarCantidadesDevueltasPorDetalle(
                                    detalleCompraId
                            );

            if (cantidadDevuelta == null) {
                cantidadDevuelta = 0L;
            }

            long cantidadDisponible =
                    detalleCompra.getCantidad()
                            - cantidadDevuelta;

            if (cantidadSolicitada > cantidadDisponible) {

                throw new IllegalArgumentException(
                        "La cantidad solicitada para el detalle "
                                + detalleCompraId
                                + " supera las unidades disponibles para devolución"
                );
            }

            double importeDetalle =
                    cantidadSolicitada
                            * detalleCompra.getPrecioUnitario();

            importeTotal += importeDetalle;

            DetalleDevolucion detalleDevolucion =
                    new DetalleDevolucion(
                            null,
                            cantidadSolicitada,
                            importeDetalle,
                            devolucion,
                            detalleCompra
                    );

            devolucion.getDetalles().add(
                    detalleDevolucion
            );
        }

        devolucion.setImporte(importeTotal);

        Devolucion guardada =
                devolucionRepository.save(devolucion);

        return convertirAResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<DevolucionResponse> listarMisDevoluciones(
            String email
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        return devolucionRepository
                .findByUsuarioIdOrderByFechaSolicitudDesc(
                        usuario.getId()
                )
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DevolucionResponse obtenerMiDevolucion(
            Long id,
            String email
    ) {

        Usuario usuario = usuarioService.obtenerPorEmail(email);

        Devolucion devolucion =
                devolucionRepository
                        .findByIdAndUsuarioId(
                                id,
                                usuario.getId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Devolución no encontrada o no pertenece al usuario"
                                )
                        );

        return convertirAResponse(devolucion);
    }

    @Transactional
    public DevolucionResponse cambiarEstado(
            Long id,
            EstadoDevolucionRequest request
    ) {

        Devolucion devolucion =
                devolucionRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Devolución no encontrada"
                                )
                        );

        EstadoDevolucion actual =
                devolucion.getEstado();

        EstadoDevolucion nuevo =
                request.getEstado();

        validarTransicion(actual, nuevo);

        if (nuevo == EstadoDevolucion.RECHAZADA) {

            if (request.getObservacion() == null
                    || request.getObservacion().isBlank()) {

                throw new IllegalArgumentException(
                        "La observación es obligatoria al rechazar una devolución"
                );
            }

            devolucion.setObservacionOperador(
                    request.getObservacion().trim()
            );

        } else if (request.getObservacion() != null
                && !request.getObservacion().isBlank()) {

            devolucion.setObservacionOperador(
                    request.getObservacion().trim()
            );
        }

        devolucion.setEstado(nuevo);

        return convertirAResponse(
                devolucionRepository.save(devolucion)
        );
    }

    @Transactional(readOnly = true)
    public DevolucionResponse obtenerPorId(Long id) {

        Devolucion devolucion =
                devolucionRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Devolución no encontrada"
                                )
                        );

        return convertirAResponse(devolucion);
    }

    @Transactional(readOnly = true)
    public Page<DevolucionResponse> filtrarAdmin(
            EstadoDevolucion estado,
            String motivo,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable
    ) {

        Specification<Devolucion> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();

        if (estado != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("estado"),
                                    estado
                            )
            );
        }

        if (motivo != null && !motivo.isBlank()) {

            String motivoBuscado =
                    motivo.trim().toLowerCase();

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(
                                            root.get("motivo")
                                    ),
                                    "%" + motivoBuscado + "%"
                            )
            );
        }

        if (fechaInicio != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("fechaSolicitud"),
                                    fechaInicio
                            )
            );
        }

        if (fechaFin != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("fechaSolicitud"),
                                    fechaFin
                            )
            );
        }

        Page<Devolucion> pagina =
                devolucionRepository.findAll(
                        specification,
                        pageable
                );

        return pagina.map(this::convertirAResponse);
    }

    private void validarTransicion(
            EstadoDevolucion actual,
            EstadoDevolucion nuevo
    ) {

        boolean valida =
                (actual == EstadoDevolucion.SOLICITADA
                        && nuevo == EstadoDevolucion.EN_REVISION)

                || (actual == EstadoDevolucion.EN_REVISION
                        && (
                        nuevo == EstadoDevolucion.APROBADA
                                || nuevo == EstadoDevolucion.RECHAZADA
                ))

                || (actual == EstadoDevolucion.APROBADA
                        && nuevo == EstadoDevolucion.COMPLETADA);

        if (!valida) {

            throw new IllegalArgumentException(
                    "Transición de estado no permitida: "
                            + actual
                            + " -> "
                            + nuevo
            );
        }
    }

    private DevolucionResponse convertirAResponse(
            Devolucion devolucion
    ) {

        DevolucionResponse response =
                new DevolucionResponse();

        response.setId(devolucion.getId());

        response.setFechaSolicitud(
                devolucion.getFechaSolicitud()
        );

        response.setEstado(
                devolucion.getEstado()
        );

        response.setMotivo(
                devolucion.getMotivo()
        );

        response.setComentario(
                devolucion.getComentario()
        );

        response.setObservacionOperador(
                devolucion.getObservacionOperador()
        );

        response.setImporte(
                devolucion.getImporte()
        );

        if (devolucion.getCompra() != null) {
            response.setCompraId(
                    devolucion.getCompra().getId()
            );
        }

        if (devolucion.getUsuario() != null) {
            response.setUsuarioId(
                    devolucion.getUsuario().getId()
            );
        }

        List<DevolucionResponse.DetalleDevolucionResponse>
                detalles =
                devolucion.getDetalles()
                        .stream()
                        .map(this::convertirDetalle)
                        .toList();

        response.setDetalles(detalles);

        return response;
    }

    private DevolucionResponse.DetalleDevolucionResponse
    convertirDetalle(
            DetalleDevolucion detalle
    ) {

        DevolucionResponse.DetalleDevolucionResponse
                response =
                new DevolucionResponse.DetalleDevolucionResponse();

        response.setId(detalle.getId());

        response.setDetalleCompraId(
                detalle.getDetalleCompra().getId()
        );

        response.setCantidad(
                detalle.getCantidad()
        );

        response.setImporte(
                detalle.getImporte()
        );

      if (detalle.getDetalleCompra().getProducto() != null) {

    response.setProductoId(
            detalle.getDetalleCompra()
                    .getProducto()
                    .getId()
    );

    response.setProductoNombre(
            detalle.getDetalleCompra()
                    .getProducto()
                    .getNombre()
    );

    response.setImagenUrl(
            detalle.getDetalleCompra()
                    .getProducto()
                    .getImagenUrl()
    );
}

        return response;
    }
}