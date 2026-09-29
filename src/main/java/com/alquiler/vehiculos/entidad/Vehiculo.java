package com.alquiler.vehiculos.entidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @Column(name = "placa", length = 20, nullable = false)
    private String placa;

    @Column(name = "color", length = 50, nullable = false)
    private String color;

    @Column(name = "idTipoVehiculo", nullable = false)
    private String idTipoVehiculo;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    // Constructor vacío requerido por JPA
    public Vehiculo() {
    }

    // Constructor con todos los atributos
    public Vehiculo(String placa, String color, String idTipoVehiculo, String estado) {
        this.placa = placa;
        this.color = color;
        this.idTipoVehiculo = idTipoVehiculo;
        this.estado = estado;
    }

    // Getters y Setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIdTipoVehiculo() {
        return idTipoVehiculo;
    }

    public void setIdTipoVehiculo(String idTipoVehiculo) {
        this.idTipoVehiculo = idTipoVehiculo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

	
}

