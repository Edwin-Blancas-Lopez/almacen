package com.edwin.almacen.controllers;

import com.edwin.almacen.docs.ProblemaDoc;
import com.edwin.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.edwin.almacen.dto.ventas.VentaRequest;
import com.edwin.almacen.dto.ventas.VentaResponse;
import com.edwin.almacen.services.ventas.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas, lógica transaccional y control de stock")
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos / Stock insuficiente",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @Operation(summary = "Listar ventas activas", description = "Retorna exclusivamente las ventas en estado 'REGISTRADA'.")
    @ApiResponse(responseCode = "200", description = "Listado de ventas registradas obtenido exitosamente")
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/canceladas")
    @Operation(summary = "Listar histórico de ventas canceladas", description = "Endpoint exclusivo para consultar las ventas que se encuentran en estado 'CANCELADA'.")
    @ApiResponse(responseCode = "200", description = "Histórico de ventas canceladas obtenido exitosamente")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta activa por ID")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "La venta no existe o está cancelada",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorIdActiva(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva venta", description = "Aplica fotografía del precio real de la BD, control de stock y transaccionalidad atómica (Rollback si falla).")
    @ApiResponse(responseCode = "201", description = "Venta registrada con éxito")
    @ApiResponse(responseCode = "404", description = "Sucursal o producto no encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.registrar(request));
    }

    @DeleteMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una venta existente", description = "Cambia el estado a 'CANCELADA' y devuelve las cantidades vendidas al stock de los productos.")
    @ApiResponse(responseCode = "204", description = "Venta cancelada exitosamente y stock devuelto")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "La venta ya se encuentra cancelada previamente",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la venta a cancelar", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        ventaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/reportes/financiero/sucursales")
    @Operation(
            summary = "Reporte financiero por sucursal",
            description = "Obtiene la facturación y las unidades vendidas por sucursal, excluyendo las ventas canceladas."
    )
    public ResponseEntity<List<ReporteVentasSucursalResponse>> generarReporteFinancieroPorSucursal() {
        return ResponseEntity.ok(
                ventaService.generarReporteFinancieroPorSucursal()
        );
    }

}