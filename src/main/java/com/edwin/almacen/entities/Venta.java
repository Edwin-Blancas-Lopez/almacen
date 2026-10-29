package com.edwin.almacen.entities;

import com.edwin.almacen.enums.EstadoVenta;
import com.edwin.almacen.exceptions.ConflictoException;
import com.edwin.almacen.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void agregarDetalle(DetalleVenta detalleVenta){
        if (detalleVenta == null)
            throw new DatoInvalidoException("El detalle de la venta es requerido");

        this.detalleVentas.add(detalleVenta);
        detalleVenta.asignarVenta(this);
    }

    public void cancelar(){
        if(this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictoException("La venta ya está cancelada");

        this.estadoVenta = EstadoVenta.CANCELADA;
    }
}
