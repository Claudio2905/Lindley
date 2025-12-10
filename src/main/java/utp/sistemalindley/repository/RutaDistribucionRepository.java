package utp.sistemalindley.repository;

import utp.sistemalindley.model.RutaDistribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaDistribucionRepository extends JpaRepository<RutaDistribucion, Integer> {

    List<RutaDistribucion> findByEstado(String estado);

    List<RutaDistribucion> findByOrigen(String origen);

    List<RutaDistribucion> findByDestino(String destino);
}
