package com.edwin.almacen.mappers;

import com.edwin.almacen.dto.productos.ProductoRequest;
import com.edwin.almacen.dto.productos.ProductoResponse;
import com.edwin.almacen.entities.Producto;
import com.edwin.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){

        return request == null
                ? null
                : Producto.crear(
                        request.nombre(),
                        categoria,
                        request.precio(),
                        request.cantidad());
    }

    public ProductoResponse entidadAResponse(Producto producto){
        return producto == null
                ? null
                : new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad()
        );
    }
}
