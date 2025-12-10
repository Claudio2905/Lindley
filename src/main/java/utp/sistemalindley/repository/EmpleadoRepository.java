package utp.sistemalindley.repository;

import utp.sistemalindley.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    List<Empleado> findByEstadoAndCargo(String estado, String cargo);

    List<Empleado> findByEstado(String estado);

    @Query("SELECT e FROM Empleado e WHERE e.cargo = 'Transportista' AND e.estado = 'Activo'")
    List<Empleado> obtenerTransportistasDisponibles();
}
