package com.edwin.almacen.services.productos;

import com.edwin.almacen.dto.productos.ProductoRequest;
import com.edwin.almacen.dto.productos.ProductoResponse;
import com.edwin.almacen.entities.Producto;
import com.edwin.almacen.enums.Categoria;
import com.edwin.almacen.exceptions.RecursoNoEncontradoException;
import com.edwin.almacen.mappers.ProductoMapper;
import com.edwin.almacen.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax) {

        log.info("Listando todos los productos");

        return productoRepository.findAll().stream()
                //.map(productoMappe -> )
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOExcepcion(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");

        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim())
        );

        productoRepository.saveAndFlush(producto);

        log.info("Nuevop producto {} registrado", producto.getNombre());

        return null;
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto =  obtenerProductoOExcepcion(id);

        log.info("Actuualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad());

        productoRepository.saveAndFlush(producto);

        log.info("Producto con id {} actualizado", id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOExcepcion(id);

        log.info("Eliminando producto con id: {}", id);

        productoRepository.delete(producto);
        productoRepository.flush();

        log.info("Producto con id {} eliminado", id);

    }

    private Producto obtenerProductoOExcepcion(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id)
        );
    }
}
