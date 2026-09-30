package com.alquiler.vehiculos.controller;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alquiler.vehiculos.entidad.Usuarios;
import com.alquiler.vehiculos.repositorio.usuario;


@RestController
@RequestMapping("/usuarios/u")
@CrossOrigin(origins = "*")
public class controladorUsuario {
	
	@Autowired
	private usuario repoUsuario;
	
	@GetMapping("/listarTodo/")
	public List<Usuarios> mostrarTodos(){
		return repoUsuario.findAll();
	}
	@GetMapping("/buscarCC/")
	public Usuarios buscarCC(@RequestParam("identificacion") String identificacion ) {
		return repoUsuario.findById(identificacion).get();
	}
	
	@PostMapping("/guardarUsuario/")
	public ResponseEntity<?> guardar(@RequestBody Usuarios u){

	    if (this.repoUsuario.existsById(u.getIdentificacion())) {
	        return ResponseEntity.status(409).body("Ya existe un usuario con esa identificación");
	    }

	    if (repoUsuario.existsByCorreo(u.getCorreo())) {
	        return ResponseEntity.status(409).body("Ya esta ese correo registrado");
	    }
	    
	    if (repoUsuario.existsByTelefono(u.getTelefono())) {
	        return ResponseEntity.status(409).body("Ya esta ese telefono registrado");
	    }
	    
	    u.setRol("USUARIO");
	    u.setFechaRegistro(LocalDateTime.now());

	    repoUsuario.save(u);
	    return ResponseEntity.ok(u);
	}
	
	@GetMapping("/login/")
	public ResponseEntity<?> login(@RequestParam("identificacion") String identificacion, @RequestParam("contrasena") String contrasena) {
	    
	    Usuarios u = repoUsuario.findByIdentificacionAndContrasena(identificacion, contrasena);

	    if (u == null) {
	        return ResponseEntity.status(401).body("identificacion o contraseña incorrectos");
	    }

	    return ResponseEntity.ok(u);
	}
	
	
	
	


}
