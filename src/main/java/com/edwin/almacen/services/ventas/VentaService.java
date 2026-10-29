package com.edwin.almacen.services.ventas;

import com.edwin.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.edwin.almacen.dto.ventas.VentaRequest;
import com.edwin.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {
    List<VentaResponse> listar();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    void cancelar(Long id);

    List<ReporteVentasSucursalResponse> generarReporteFinancieroPorSucursal();
}
