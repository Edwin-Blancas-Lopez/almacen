
package com.edwin.almacen.services.ventas;

import com.edwin.almacen.dto.ventas.DetalleVentaRequest;
import com.edwin.almacen.entities.DetalleVenta;
import com.edwin.almacen.entities.Producto;
import com.edwin.almacen.entities.Venta;
import com.edwin.almacen.exceptions.RecursoNoEncontradoException;
import com.edwin.almacen.mappers.DetalleVentaMapper;
import com.edwin.almacen.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final ProductoRepository productoRepository;
    private final DetalleVentaMapper detalleVentaMapper;

    @Override
    @Transactional
    public List<DetalleVenta> prepararDetalles(
            List<DetalleVentaRequest> productos,
            Venta venta
    ) {
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaRequest itemDto : productos) {

            Producto producto = productoRepository.findById(itemDto.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + itemDto.idProducto()
                    ));

            int cantidadSolicitada =
                    Math.toIntExact(itemDto.cantidadProducto());

            producto.descontarCantidad(cantidadSolicitada);

            DetalleVenta detalle =
                    detalleVentaMapper.requestAEntidad(producto, itemDto);

            venta.agregarDetalle(detalle);
            detalles.add(detalle);
        }

        return detalles;
    }
}
