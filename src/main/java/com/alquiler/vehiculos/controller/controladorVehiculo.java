package com.alquiler.vehiculos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alquiler.vehiculos.entidad.Alquiler;
import com.alquiler.vehiculos.entidad.Vehiculo;
import com.alquiler.vehiculos.repositorio.alquiler;
import com.alquiler.vehiculos.repositorio.vehiculo;

@RestController
@RequestMapping("/vehiculos/v")
@CrossOrigin(origins = "*")
public class controladorVehiculo {

    @Autowired
    private final vehiculo vehiculoRepo;
    
    @Autowired
    private final alquiler alquilerRepo;

    controladorVehiculo(vehiculo vehiculoRepo, alquiler alquilerRepo) {
        this.vehiculoRepo = vehiculoRepo;
		this.alquilerRepo = alquilerRepo;
    }

    @GetMapping("/listarTodo/")
    public List<Vehiculo> mostrarTodos() {
        return vehiculoRepo.findAll();
    }

    @GetMapping("/buscarPlaca/")
    public Vehiculo buscarPlaca(@RequestParam String placa) {
        return vehiculoRepo.findById(placa).orElse(null);
    }

    @PostMapping("/buscarColor/")
    public List<Vehiculo> buscarColor(@RequestParam("color") String color) {
        return this.vehiculoRepo.findByColor(color);
    }

    @PostMapping("/buscarTipo/")
    public List<Vehiculo> buscarTipo(@RequestParam("idTipoVehiculo") String idTipoVehiculo) {
        return this.vehiculoRepo.findByIdTipoVehiculo(idTipoVehiculo);
    }

    @GetMapping("/buscarEstado/")
    public List<Vehiculo> buscarEstado(@RequestParam("estado") String estado) {
        return this.vehiculoRepo.findByEstado(estado);
    }

    @PostMapping("/guardar/")
    public ResponseEntity<Vehiculo> guardarVehiculo(@RequestBody Vehiculo vehiculo) {
        vehiculoRepo.save(vehiculo);
        return ResponseEntity.ok(vehiculo);
    }

   
    @PostMapping("/buscarDisponiblesTipo/")
    public List<Vehiculo> buscarDisponiblesTipo(@RequestParam("idTipoVehiculo") String idTipoVehiculo) {
        return this.vehiculoRepo.findByIdTipoVehiculoAndEstado(idTipoVehiculo, "disponible");
    }

	
	@PostMapping("/eliminarVehiculo")
    public Optional<Vehiculo> eliminarVehiculo(@RequestBody String n){
    	Vehiculo v = this.vehiculoRepo.findById(n).get();
    	List<Alquiler> a = this.alquilerRepo.findByVehiculo(v);
    	
    	for(int i=0 ; i<a.size(); i++) {
    		this.alquilerRepo.deleteById(a.get(i).getIdAlquiler());
    	}
    	
    	this.vehiculoRepo.deleteById(n);
    return Optional.empty();
    }
    
}