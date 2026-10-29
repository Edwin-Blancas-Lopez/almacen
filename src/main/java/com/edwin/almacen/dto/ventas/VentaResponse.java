package com.edwin.almacen.dto.ventas;

import com.edwin.almacen.dto.sucursales.SucursalResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos de una venta")
public record VentaResponse(

        @Schema(description = "Identificador de la venta", example = "1")
        Long id,

        @Schema(description = "Fecha de la venta", example = "Laptop Gamer")
        String fecha,

        @Schema(description = "Estado de la venta", example = "Registrada")
        Integer estado,

        @Schema(description = "Sucursal donde se realizo la venta")
        SucursalResponse sucursal,

        @Schema(description = "Lista de productos de la venta")
        List<DetalleVentaResponse> detalles,

        @Schema(description = "Total de la venta", example = "1500.00")
        BigDecimal total
) {
}
