package com.edwin.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "Datos necesarios para crear una venta")
public record VentaRequest(
        @Schema(description = "ID de la sucursal", example = "1")
        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal debe ser positivo")
        Long idSucursal,

        @Schema(description = "Lista de productos de la venta")
        @NotEmpty(message = "La lista de productos es requerida y no debe estar vacia")
        List<@Valid DetalleVentaRequest> productos
) {}

