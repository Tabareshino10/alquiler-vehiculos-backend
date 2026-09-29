package com.alquiler.vehiculos.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    // LÓGICA DE SOLICITUD: Asigna estado "pendiente de entrega" y pasa vehículo a "alquilado"
    @PostMapping("/guardar/")
    public ResponseEntity<?> guardarAlquiler(@RequestBody Alquiler a) {
        // 1. Validar que enviaron la placa
        if (a.getVehiculo() == null || a.getVehiculo().getPlaca() == null) {
            return ResponseEntity.badRequest().body("Debe indicar la placa del vehículo.");
        }

        // 2. Buscar el vehículo en la base de datos
        Vehiculo v = this.vehiculoRepo.findById(a.getVehiculo().getPlaca()).orElse(null);
        if (v == null || !"disponible".equalsIgnoreCase(v.getEstado())) {
            return ResponseEntity.badRequest().body("El vehículo no está disponible.");
        }

        // 3. Actualizar estado del vehículo a "alquilado"
        v.setEstado("alquilado");
        this.vehiculoRepo.save(v);

        // 4. Crear el nuevo alquiler con 'new' como lo hicieron en clase
        Alquiler nuevoAlquiler = new Alquiler(
        	    null,                   // idAlquiler (lo genera la BD)
        	    a.getIdUsuario(),       // idUsuario
        	    v,                      // vehiculo
        	    a.getFechaInicio(),     // fechaInicio
        	    a.getFechaEntregaP(),   // fechaEntregaP
        	    null,                   // fechaEntregaR (aún no se ha devuelto)
        	    a.getValorAlquiler(),   // valorAlquiler
        	    "pendiente"  // estado
        );

        
        Alquiler guardado = this.alquilerRepo.save(nuevoAlquiler);
        return ResponseEntity.ok(guardado);
    }
    // NUEVO: Cancelar alquiler por idAlquiler (Cliente)
    @PostMapping("/cancelar/")
    public ResponseEntity<?> cancelarAlquiler(@RequestParam("idAlquiler") Integer idAlquiler) {
        Alquiler a = alquilerRepo.findById(idAlquiler).orElse(null);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        a.setEstado("cancelado");

        // Liberar el vehículo
        Vehiculo v = a.getVehiculo();
        if (v != null) {
            v.setEstado("disponible");
            vehiculoRepo.save(v);
        }

        alquilerRepo.save(a);
        return ResponseEntity.ok("Alquiler cancelado correctamente.");
    }

    // NUEVO: Administrador entrega vehículo buscando por Placa
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

    // NUEVO: Administrador recibe devolución buscando por idAlquiler
    @PostMapping("/devolver/")
    public ResponseEntity<?> registrarDevolucion(@RequestParam("idAlquiler") Integer idAlquiler) {
        Alquiler a = alquilerRepo.findById(idAlquiler).orElse(null);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }

        Date fechaReal = new Date();
        a.setFechaEntregaR(fechaReal);

        // Cálculo de recargo si la fecha real supera la fecha previa estimada
        if (fechaReal.after(a.getFechaEntregaP())) {
            long milisegundos = fechaReal.getTime() - a.getFechaEntregaP().getTime();
            long diasAdicionales = TimeUnit.DAYS.convert(milisegundos, TimeUnit.MILLISECONDS);

            if (diasAdicionales > 0) {
                BigDecimal tarifaDiaria = new BigDecimal("50000"); // Define o ajusta la tarifa por día
                BigDecimal recargo = tarifaDiaria.multiply(new BigDecimal(diasAdicionales));
                a.setValorAlquiler(a.getValorAlquiler().add(recargo));
            }
        }

        a.setEstado("finalizado");

        // Liberar el vehículo a disponible
        Vehiculo v = a.getVehiculo();
        if (v != null) {
            v.setEstado("disponible");
            vehiculoRepo.save(v);
        }

        Alquiler alquilerFinalizado = alquilerRepo.save(a);
        return ResponseEntity.ok(alquilerFinalizado);
    }
}