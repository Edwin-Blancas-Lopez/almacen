package com.edwin.almacen.repositories.specs;

import com.edwin.almacen.entities.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.edwin.almacen.enums.Categoria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class ProductoSpecs {
    public static Specification<Producto> filtros(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        return (Root<Producto> productoRoot, CriteriaQuery<?> consulta, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.trim().isEmpty()) {
                String nombreNormalizado = nombre.trim().toLowerCase();
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(productoRoot.get("nombre")),
                        "%" + nombreNormalizado + "%"
                ));
            }

            if (categoria != null && !categoria.trim().isEmpty()) {
                try {
                    Categoria categoriaEnum = Categoria.obtenerCategoriaPorDescripcion(categoria);
                    if (categoriaEnum != null) {
                        predicates.add(criteriaBuilder.equal(productoRoot.get("categoria"), categoriaEnum));
                    } else {
                        log.warn("La categoría '{}' no fue encontrada. Omitiendo el filtro para mostrar todos los registros.", categoria);
                    }
                } catch (Exception e) {
                    log.warn("La categoría '{}' no es válida ({}. Omitiendo el filtro.", categoria, e.getMessage());
                }
            }

            if (precioMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productoRoot.get("precio"), precioMin));
            }

            if (precioMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productoRoot.get("precio"), precioMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
