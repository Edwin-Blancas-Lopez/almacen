package com.edwin.almacen.mappers;

import com.edwin.almacen.dto.ventas.DetalleVentaRequest;
import com.edwin.almacen.dto.ventas.DetalleVentaResponse;
import com.edwin.almacen.entities.DetalleVenta;
import com.edwin.almacen.entities.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(Producto producto, DetalleVentaRequest request
    ) {
        if (producto == null || request == null) {
            return null;
        }

        int cantidad = Math.toIntExact(request.cantidadProducto());

        return DetalleVenta.builder()
                .producto(producto)
                .cantidadProducto(cantidad)
                .precioProducto(producto.getPrecio())
                .build();
    }


    public DetalleVentaResponse entidadAResponse(DetalleVenta detalle) {
        if (detalle == null) {
            return null;
        }

        String nombreProducto = detalle.getProducto() != null
                ? detalle.getProducto().getNombre()
                : null;

        Long idProducto = detalle.getProducto() != null
                ? detalle.getProducto().getId()
                : null;

        BigDecimal cantidadDecimal =
                BigDecimal.valueOf(detalle.getCantidadProducto());

        BigDecimal subtotal = detalle.getPrecioProducto()
                .multiply(cantidadDecimal);

        return new DetalleVentaResponse(
                idProducto,
                nombreProducto,
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                subtotal
        );
    }
}
