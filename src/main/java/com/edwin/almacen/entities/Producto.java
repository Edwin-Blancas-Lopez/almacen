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
@Table(name = "PRODUCTOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 30)
    private String nombre;

    @Column(name = "CATEGORIA", nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    private static void validarDatos(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad) {
        StringCustomUtils.validarTamanio(nombre, 5, 30,
                "El nombre es requerido y debe tener entre 5 y 30 caracteres");

        if (categoria == null)
            throw new DatoInvalidoException("La categoria es requerida");

        ValoresNumericosUtils.validarBigDecilmalPositivo(precio,
                "El precio es requerido y debe ser positivo");

        ValoresNumericosUtils.validarNumeroPositivo(cantidad,
                "La cantidad es requerida y debe ser positiva");
    }

    public void actualizar (
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad) {

        validarDatos(nombre, categoria, precio, cantidad);

        this.nombre = nombre.trim();
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public void aumentarCantidad(int cantidad){
        ValoresNumericosUtils.validarNumeroPositivo(
                cantidad,
                "La cantidad debe ser positiva");

        this.cantidad += cantidad;
    }

    public void descontarCantidad(int cantidad){
        ValoresNumericosUtils.validarNumeroPositivo(
                cantidad,
                "La cantidad debe ser positiva");

        if (cantidad > this.cantidad)
            throw new DatoInvalidoException("La cantidad debe ser menor o igual a la cantidad actual");
        this.cantidad -= cantidad;
    }

    public static Producto crear(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad) {

        validarDatos(nombre, categoria, precio, cantidad);

        return Producto.builder()
                .nombre(nombre.trim())
                .categoria(categoria)
                .precio(precio)
                .cantidad(cantidad)
                .build();

        //return new Producto(null, nombre, categoria, precio. cantidad);
    }
}
