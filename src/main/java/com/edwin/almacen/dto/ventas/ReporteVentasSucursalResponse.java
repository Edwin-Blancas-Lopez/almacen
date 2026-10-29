package com.edwin.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Reporte financiero de una sucursal")
public record ReporteVentasSucursalResponse(
        @Schema(description = "ID de la sucursal", example = "1")
        Long idSucursal,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal Centro")
        String nombreSucursal,

        @Schema(description = "Total facturado por la sucursal", example = "350.00")
        BigDecimal totalFacturado,

        @Schema(description = "Total de unidades vendidas", example = "5")
        Long unidadesVendidas

) {

}
