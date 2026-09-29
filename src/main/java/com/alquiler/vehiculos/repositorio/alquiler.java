package com.alquiler.vehiculos.repositorio;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alquiler.vehiculos.entidad.Alquiler;
import com.alquiler.vehiculos.entidad.Vehiculo;



@Repository
public interface alquiler extends JpaRepository<Alquiler, Integer> {

	public List<Alquiler> findByIdUsuario(Integer idUsuario);

	public List<Alquiler> findByVehiculoPlaca(String placa);

	public List<Alquiler> findByEstado(String estado);

	public List<Alquiler> findByVehiculoPlacaAndEstado(String placa, String estado);
	
	public List<Alquiler> findByVehiculo(Vehiculo vehiculo);
	
}