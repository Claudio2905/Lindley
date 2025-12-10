package utp.sistemalindley.repository;

import utp.sistemalindley.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {

    List<Entrega> findByEstadoEntrega(String estadoEntrega);

    List<Entrega> findByIdPedido(Integer idPedido);

    List<Entrega> findByIdEmpleado(Integer idEmpleado);

    @Query("SELECT e FROM Entrega e WHERE e.estadoEntrega IN ('Programada', 'En Tránsito') " +
            "ORDER BY e.fechaSalida DESC")
    List<Entrega> obtenerEntregasPendientes();
}
