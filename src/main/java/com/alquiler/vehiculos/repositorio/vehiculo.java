package com.alquiler.vehiculos.repositorio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.alquiler.vehiculos.entidad.Vehiculo;


@Repository
public interface vehiculo extends JpaRepository<Vehiculo, String> {

	public List<Vehiculo> findByColor(String color);

	public List<Vehiculo> findByIdTipoVehiculo(String idTipoVehiculo);

	public List<Vehiculo> findByEstado(String estado);
	
	List<Vehiculo> findByIdTipoVehiculoAndEstado(String idTipoVehiculo, String estado);
}