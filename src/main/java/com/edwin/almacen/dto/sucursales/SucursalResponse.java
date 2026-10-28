package com.edwin.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de una sucursal")
public record SucursalResponse(
        @Schema(description = "Identificador de la sucursal", example = "1")
        Long id,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal norte")
        String nombre,

        @Schema(description = "Direccion de la sucursal", example = "Calle 5 #10")
        String direccion
) {
}
