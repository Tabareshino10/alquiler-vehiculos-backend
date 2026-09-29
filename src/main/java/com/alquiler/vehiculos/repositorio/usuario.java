package com.alquiler.vehiculos.repositorio;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alquiler.vehiculos.entidad.Usuarios;



@Repository
public interface usuario extends JpaRepository<Usuarios, String > {
	
public List<Usuarios> findByNombreCompleto(String nombreCompleto);


public boolean existsByCorreo(String correo);

public boolean existsByTelefono(String telefono);

Usuarios findByIdentificacionAndContrasena(String identificacion, String contrasena);


	
}

