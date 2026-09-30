package com.alquiler.vehiculos.entidad;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alquiler")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Integer idAlquiler;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "placa", referencedColumnName = "placa", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

   
    @Column(name = "fechaEntregaP", nullable = false)
    private Date fechaEntregaP;

	@Column(name = "fechaEntregaR")
    private Date fechaEntregaR;

    @Column(name = "valorAlquiler", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorAlquiler;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;
    
   

    // Constructor vacío requerido por JPA
    public Alquiler() {
    }

    public Alquiler(Integer idAlquiler, Integer idUsuario, Vehiculo vehiculo, Date fechaInicio, Date fechaEntregaP,
			Date fechaEntregaR, BigDecimal valorAlquiler, String estado) {
		super();
		this.idAlquiler = idAlquiler;
		this.idUsuario = idUsuario;
		this.vehiculo = vehiculo;
		this.fechaInicio = fechaInicio;
		this.fechaEntregaP = fechaEntregaP;
		this.fechaEntregaR = fechaEntregaR;
		this.valorAlquiler = valorAlquiler;
		this.estado = estado;
	}
    
    // Getters y Setters
    public Integer getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(Integer idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaEntregaP() {
        return fechaEntregaP;
    }

    public void setFechaEntregaP(Date fechaEntregaP) {
        this.fechaEntregaP = fechaEntregaP;
    }

    public Date getFechaEntregaR() {
        return fechaEntregaR;
    }

    public void setFechaEntregaR(Date fechaEntregaR) {
        this.fechaEntregaR = fechaEntregaR;
    }

    public BigDecimal getValorAlquiler() {
        return valorAlquiler;
    }

    public void setValorAlquiler(BigDecimal valorAlquiler) {
        this.valorAlquiler = valorAlquiler;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
