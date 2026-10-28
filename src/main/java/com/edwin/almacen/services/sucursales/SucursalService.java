package com.edwin.almacen.services.sucursales;

import com.edwin.almacen.dto.productos.ProductoRequest;
import com.edwin.almacen.dto.productos.ProductoResponse;
import com.edwin.almacen.dto.sucursales.SucursalRequest;
import com.edwin.almacen.dto.sucursales.SucursalResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SucursalService {

    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);

}