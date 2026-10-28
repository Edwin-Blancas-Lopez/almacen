package com.edwin.almacen.services.sucursales;

import com.edwin.almacen.dto.sucursales.SucursalRequest;
import com.edwin.almacen.dto.sucursales.SucursalResponse;
import com.edwin.almacen.entities.Sucursal;
import com.edwin.almacen.exceptions.RecursoNoEncontradoException;
import com.edwin.almacen.mappers.SucursalMapper;
import com.edwin.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{
    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar(){
        log.info("Listano todas las sucursalees");

        return  sucursalRepository.findAll().stream().map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {

        log.info("Registrando nueva sucursal...");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);
        sucursalRepository.saveAndFlush(sucursal);

        log.info("Nueva sucursal registrada: {}", sucursal.getNombre());

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {

        Sucursal sucursal = obtenerSucursalOException(id);

        validarCambiosUnicos(request, id);

        log.info("Actualizando sucursal con id: {}", id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion()
        );

        sucursalRepository.saveAndFlush(sucursal);

        log.info("Sucursal con id {} actualizada", id);
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);

        log.info("Eliminando sucursal con id {}", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Sucursal con id {} eliminada", id);

    }

    private Sucursal obtenerSucursalOException(Long id){
        log.info("Buscando sucursal con id: " + id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: {}" + id
                )
        );
    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre unico...");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new RuntimeException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("Validando cambio en nombre unico...");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new RuntimeException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }


}
