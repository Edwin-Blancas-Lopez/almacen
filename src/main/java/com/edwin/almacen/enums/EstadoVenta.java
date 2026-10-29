package com.edwin.almacen.enums;


import com.edwin.almacen.exceptions.DatoInvalidoException;
import com.edwin.almacen.utils.StringCustomUtils;
import com.edwin.almacen.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {

    REGISTRADA (1L, "Registrada"),
    CANCELADA (0L, "Cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoVenta estadoVenta : values()) {
            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }

        throw new DatoInvalidoException("No existe un estado de venta con la descripcion: " + descripcion);
    }

    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo){
        ValoresNumericosUtils.validarNumeroRequerido(codigo, "El código es requerido");

        for (EstadoVenta estadoVenta : values()) {
            if (estadoVenta.codigo.equals(codigo))
                return estadoVenta;
        }

        throw new DatoInvalidoException("No existe un estado de venta con el codigo: " + codigo);
    }
}
