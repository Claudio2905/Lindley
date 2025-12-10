package utp.sistemalindley.repository;

import utp.sistemalindley.model.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {

    List<Incidencia> findByIdEntrega(Integer idEntrega);

    List<Incidencia> findByEstado(String estado);

    @Query("SELECT i FROM Incidencia i ORDER BY i.fechaRegistro DESC")
    List<Incidencia> obtenerIncidenciasRecientes();
}
