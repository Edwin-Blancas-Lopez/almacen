package com.edwin.almacen.services.ventas;

import com.edwin.almacen.dto.ventas.DetalleVentaRequest;
import com.edwin.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.edwin.almacen.dto.ventas.VentaRequest;
import com.edwin.almacen.dto.ventas.VentaResponse;
import com.edwin.almacen.entities.DetalleVenta;
import com.edwin.almacen.entities.Producto;
import com.edwin.almacen.entities.Sucursal;
import com.edwin.almacen.entities.Venta;
import com.edwin.almacen.enums.EstadoVenta;
import com.edwin.almacen.exceptions.RecursoNoEncontradoException;
import com.edwin.almacen.mappers.DetalleVentaMapper;
import com.edwin.almacen.mappers.VentaMapper;
import com.edwin.almacen.repositories.ProductoRepository;
import com.edwin.almacen.repositories.SucursalRepository;
import com.edwin.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService{
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        return ventaRepository.findByEstadoVenta(EstadoVenta.REGISTRADA).stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    //----------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        return ventaRepository.findByEstadoVenta(EstadoVenta.CANCELADA).stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }
    //------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        Venta venta = obtenerVentaOException(id);
        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new RecursoNoEncontradoException("La venta con id " + id + " se encuentra cancelada.");
        }
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> generarReporteFinancieroPorSucursal() {
        log.info("Generando reporte financiero por sucursal");

        return ventaRepository.generarReportePorSucursal(
                EstadoVenta.REGISTRADA
        );
    }


    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info(
                "Iniciando registro de venta para la sucursal ID: {}",
                request.idSucursal()
        );

        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + request.idSucursal()
                ));

        Venta venta = ventaMapper.requestAEntidad(request, sucursal);

        for (DetalleVentaRequest itemDto : request.productos()) {

            Producto producto = productoRepository.findById(itemDto.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + itemDto.idProducto()
                    ));

            int cantidad = Math.toIntExact(itemDto.cantidadProducto());

            // Descontamos el stock del producto.
            producto.descontarCantidad(cantidad);

            // El mapper construye el detalle con el precio actual del producto.
            DetalleVenta detalle = detalleVentaMapper.requestAEntidad(
                    producto,
                    cantidad
            );

            // Agregamos el detalle a la venta.
            venta.agregarDetalle(detalle);
        }

        Venta ventaGuardada = ventaRepository.save(venta);

        log.info(
                "Venta registrada con éxito bajo el ID: {}",
                ventaGuardada.getId()
        );

        return ventaMapper.entidadAResponse(ventaGuardada);
    }

    @Override
    public void cancelar(Long id) {
        log.info("Iniciando cancelación de la venta con ID: {}", id);

        Venta venta = obtenerVentaOException(id);
        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
            productoRepository.save(producto);
            log.info("Devolución de stock: Se sumaron {} unidades al producto ID: {}", detalle.getCantidadProducto(), producto.getId());
        }

        ventaRepository.save(venta);
        log.info("Venta con ID: {} cancelada exitosamente.", id);
    }

    private Venta obtenerVentaOException(Long id){
        log.info("Buscando venta con id: " + id);

        return ventaRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Venta no encontrada con id: " + id
                )
        );
    }
}
