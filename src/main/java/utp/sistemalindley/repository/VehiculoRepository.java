package utp.sistemalindley.repository;

import utp.sistemalindley.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    List<Vehiculo> findByEstado(String estado);

    Vehiculo findByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v WHERE v.estado = 'Disponible'")
    List<Vehiculo> obtenerVehiculosDisponibles();
}
