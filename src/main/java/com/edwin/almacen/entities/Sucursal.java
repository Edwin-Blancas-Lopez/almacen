package com.edwin.almacen.entities;


import com.edwin.almacen.enums.Categoria;
import com.edwin.almacen.exceptions.DatoInvalidoException;
import com.edwin.almacen.utils.StringCustomUtils;
import com.edwin.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "SUCURSALES")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    private String direccion;

    public static void validarDatos(String nombre, String direccion){
        StringCustomUtils.validarTamanio(nombre, 5, 50,
                "El nombre es requerido y debe tener entre 5 y 50 caracteres");

        StringCustomUtils.validarTamanio(direccion, 10, 150,
                "La direccion es requerida y debe tener entre 10 y 150");
    }

    public void actualizar(String nombre, String direccion ){
        validarDatos(nombre, direccion);

        this.nombre = nombre.trim();
        this.direccion = direccion.trim();
    }

    public static Sucursal crear (String nombre, String direccion){
        validarDatos(nombre, direccion);

        return Sucursal.builder().nombre(nombre.trim()).direccion(direccion.trim()).build();
    }
}
