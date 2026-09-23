package com.alquiler.vehiculos.entidad;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuarios {

	@Id
	@Column(name = "cc")
	private String identificacion;

	@Column(name = "nombre", length = 50, nullable = false)
	private String nombreCompleto;

	@Column(name = "fechaExpeLicencia", nullable = false)
	private LocalDate fechaLicencia;

	@Column(name = "categoriaLicencia", length = 50, nullable = false)
	private String categoria;

	@Column(name = "vigenciaLicencia", nullable = false)
	private LocalDate vigencia;

	@Column(name = "correo", nullable = false, unique = true)
	private String correo;

	@Column(name = "numeroTelefono", nullable = false)
	private String telefono;

	@Column(name = "contrasena", length = 50, nullable = false)
	private String contrasena;

	@Column(name = "rol", nullable = false, length = 20)
	private String rol;

	@Column(name = "fecha_registro", nullable = false)
	private LocalDateTime fechaRegistro;

	public Usuarios() {}

	public Usuarios(String identificacion, String nombreCompleto, LocalDate fechaLicencia, String categoria,
			LocalDate vigencia, String correo, String telefono, String contrasena, String rol,
			LocalDateTime fechaRegistro) {
		super();
		this.identificacion = identificacion;
		this.nombreCompleto = nombreCompleto;
		this.fechaLicencia = fechaLicencia;
		this.categoria = categoria;
		this.vigencia = vigencia;
		this.correo = correo;
		this.telefono = telefono;
		this.contrasena = contrasena;
		this.rol = rol;
		this.fechaRegistro = fechaRegistro;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public LocalDate getFechaLicencia() {
		return fechaLicencia;
	}

	public void setFechaLicencia(LocalDate fechaLicencia) {
		this.fechaLicencia = fechaLicencia;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public LocalDate getVigencia() {
		return vigencia;
	}

	public void setVigencia(LocalDate vigencia) {
		this.vigencia = vigencia;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	

	
	
}
