package com.edwin.almacen.repositories;

import com.edwin.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    //SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(:NOMBRE);
    boolean existsByNombreIgnoreCase(String nombre);

    //SELECT COUNT(*) FROM SUCURSALES WHERE LOWER(NOMBRE) = LOWER(:NOMBRE) AND ID_SUCURSAL <> :ID;
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
