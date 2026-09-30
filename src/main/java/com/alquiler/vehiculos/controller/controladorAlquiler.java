package com.alquiler.vehiculos.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/alquileres/a")
@CrossOrigin(origins = "http://localhost:4200/")
public class controladorAlquiler {

    @Autowired
    private final alquiler alquilerRepo;

    @Autowired
    private vehiculo vehiculoRepo;

    controladorAlquiler(alquiler alquilerRepo) {
        this.alquilerRepo = alquilerRepo;
    }

    @GetMapping("/listarTodo/")
    public List<Alquiler> mostrarTodos() {
        return alquilerRepo.findAll();
    }
    
 
    @GetMapping("/listarPorUsuario/")
    public ResponseEntity<List<Alquiler>> listarPorUsuario(@RequestParam("idUsuario") Integer identificacion) {
        List<Alquiler> alquileresUsuario = alquilerRepo.findByIdUsuario(identificacion);
        return ResponseEntity.ok(alquileresUsuario);
    }

    @PostMapping("/buscarId/")
    public Alquiler buscarId(@RequestParam Integer idAlquiler) {
        return alquilerRepo.findById(idAlquiler).orElse(null);
    }

    @PostMapping("/buscarUsuario/")
    public List<Alquiler> buscarPorUsuario(@RequestParam("idUsuario") Integer idUsuario) {
        return this.alquilerRepo.findByIdUsuario(idUsuario);
    }

    @PostMapping("/buscarPlaca/")
    public List<Alquiler> buscarPorPlaca(@RequestParam("placa") String placa) {
        return this.alquilerRepo.findByVehiculoPlaca(placa);
    }

    @PostMapping("/buscarEstado/")
    public List<Alquiler> buscarEstado(@RequestParam("estado") String estado) {
        return this.alquilerRepo.findByEstado(estado);
    }

    @PostMapping("/guardar/")
    public ResponseEntity<?> guardarAlquiler(@RequestBody Alquiler a) {
        
        if (a.getVehiculo() == null || a.getVehiculo().getPlaca() == null) {
            return ResponseEntity.badRequest().body("Debe indicar la placa del vehículo.");
        }

        Vehiculo v = this.vehiculoRepo.findById(a.getVehiculo().getPlaca()).orElse(null);
        if (v == null || !"disponible".equalsIgnoreCase(v.getEstado())) {
            return ResponseEntity.badRequest().body("El vehículo no está disponible.");
        }

        v.setEstado("alquilado");
        this.vehiculoRepo.save(v);

        Alquiler nuevoAlquiler = new Alquiler(
        	    null,                  
        	    a.getIdUsuario(),      
        	    v,                    
        	    a.getFechaInicio(),   
        	    a.getFechaEntregaP(),
        	    null,                   
        	    a.getValorAlquiler(),
        	    "pendiente"
        );

        
        Alquiler guardado = this.alquilerRepo.save(nuevoAlquiler);
        return ResponseEntity.ok(guardado);
    }
    
    @PostMapping("/cancelar/")
    public ResponseEntity<?> cancelarAlquiler(@RequestParam("idAlquiler") Integer idAlquiler) {
        Alquiler a = alquilerRepo.findById(idAlquiler).orElse(null);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        a.setEstado("cancelado");

        Vehiculo v = a.getVehiculo();
        if (v != null) {
            v.setEstado("disponible");
            vehiculoRepo.save(v);
        }

        alquilerRepo.save(a);
        return ResponseEntity.ok("Alquiler cancelado correctamente.");
    }


    @PostMapping("/entregarPorPlaca/")
    public ResponseEntity<?> entregarPorPlaca(@RequestParam("placa") String placa) {
        List<Alquiler> lista = alquilerRepo.findByVehiculoPlacaAndEstado(placa, "pendiente");
        if (lista.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay alquileres pendientes de entrega para esta placa.");
        }

        Alquiler a = lista.get(0);
        a.setEstado("alquilado");
        alquilerRepo.save(a);

        return ResponseEntity.ok("Estado del alquiler cambiado a entregado.");
    }


    @PostMapping("/devolver/")
    public ResponseEntity<?> registrarDevolucion(@RequestParam("idAlquiler") Integer idAlquiler, @RequestParam("fechaEntregaR") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaReal) {
        
        Alquiler a = alquilerRepo.findById(idAlquiler).orElse(null);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        // Asignamos la fecha que llegó desde el frontend en lugar de new Date()
        a.setFechaEntregaR(fechaReal);

        // Cálculo de recargo si la fecha real supera la fecha previa estimada
        if (a.getFechaEntregaP() != null && fechaReal.after(a.getFechaEntregaP())) {
            long milisegundos = fechaReal.getTime() - a.getFechaEntregaP().getTime();
            long diasAdicionales = TimeUnit.DAYS.convert(milisegundos, TimeUnit.MILLISECONDS);

            if (diasAdicionales > 0) {
                BigDecimal tarifaDiaria = new BigDecimal("50000"); 
                BigDecimal recargo = tarifaDiaria.multiply(new BigDecimal(diasAdicionales));
                a.setValorAlquiler(a.getValorAlquiler().add(recargo));
            }
        }

        a.setEstado("finalizado");

        Vehiculo v = a.getVehiculo();
        if (v != null) {
            v.setEstado("disponible");
            vehiculoRepo.save(v);
        }

        Alquiler alquilerFinalizado = alquilerRepo.save(a);
        return ResponseEntity.ok(alquilerFinalizado);
    }
    
 // NUEVO: Eliminar alquiler por idAlquiler
    @PostMapping("/eliminar/")
    public ResponseEntity<?> eliminarAlquiler(@RequestParam("idAlquiler") Integer idAlquiler) {
        Alquiler a = alquilerRepo.findById(idAlquiler).orElse(null);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        alquilerRepo.delete(a);
        return ResponseEntity.ok("Alquiler eliminado correctamente.");
    }
}