package utp.sistemalindley.repository;

import utp.sistemalindley.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByEstado(String estado);

    List<Producto> findByNombreContainingIgnoreCaseAndEstado(String nombre, String estado);

    List<Producto> findByCategoriaContainingIgnoreCaseAndEstado(String categoria, String estado);

    @Query("SELECT p FROM Producto p WHERE " +
            "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR :nombre IS NULL) AND " +
            "(LOWER(p.categoria) LIKE LOWER(CONCAT('%', :categoria, '%')) OR :categoria IS NULL) AND " +
            "p.estado = 'Activo'")
    List<Producto> buscarProductos(@Param("nombre") String nombre,
                                   @Param("categoria") String categoria);
}

