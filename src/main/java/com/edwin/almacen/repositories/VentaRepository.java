package com.edwin.almacen.repositories;

import com.edwin.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.edwin.almacen.entities.Venta;
import com.edwin.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);

    @Query("""
        SELECT new com.edwin.almacen.dto.ventas.ReporteVentasSucursalResponse(
            s.id,
            s.nombre,
            COALESCE(SUM(d.precioProducto * d.cantidadProducto), 0.0000BD),
            COALESCE(SUM(d.cantidadProducto), 0L))
        FROM Sucursal s
        LEFT JOIN Venta v
            ON v.sucursal = s
            AND v.estadoVenta = :estado
        LEFT JOIN v.detalleVentas d
        GROUP BY s.id, s.nombre
        ORDER BY s.nombre
        """)
    List<ReporteVentasSucursalResponse> generarReportePorSucursal(
            @Param("estado") EstadoVenta estado
    );

}
