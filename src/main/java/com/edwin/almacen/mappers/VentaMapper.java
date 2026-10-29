
package com.edwin.almacen.mappers;

import com.edwin.almacen.dto.ventas.DetalleVentaResponse;
import com.edwin.almacen.dto.ventas.VentaRequest;
import com.edwin.almacen.dto.ventas.VentaResponse;
import com.edwin.almacen.entities.Sucursal;
import com.edwin.almacen.entities.Venta;
import com.edwin.almacen.enums.EstadoVenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class VentaMapper {

    private final SucursalMapper sucursalMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    public VentaMapper(
            SucursalMapper sucursalMapper,
            DetalleVentaMapper detalleVentaMapper) {
        this.sucursalMapper = sucursalMapper;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    public Venta requestAEntidad(
            VentaRequest request,
            Sucursal sucursal
    ) {
        if (request == null) {
            return null;
        }

        return Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();
    }

    public VentaResponse entidadAResponse(Venta venta) {
        if (venta == null) {
            return null;
        }

        List<DetalleVentaResponse> detallesResponse =
                venta.getDetalleVentas().stream()
                        .map(detalleVentaMapper::entidadAResponse)
                        .toList();

        BigDecimal totalVenta = detallesResponse.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFecha() != null
                        ? venta.getFecha().toString()
                        : null,
                venta.getEstadoVenta() != null
                        ? venta.getEstadoVenta().getCodigo().intValue()
                        : null,
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detallesResponse,
                totalVenta
        );
    }
}
