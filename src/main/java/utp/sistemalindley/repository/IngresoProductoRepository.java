package utp.sistemalindley.repository;

import utp.sistemalindley.model.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoProductoRepository extends JpaRepository<IngresoProducto, Integer> {

    List<IngresoProducto> findByIdProducto(Integer idProducto);

    List<IngresoProducto> findByIdAlmacen(Integer idAlmacen);

    @Query("SELECT i FROM IngresoProducto i ORDER BY i.fechaIngreso DESC")
    List<IngresoProducto> obtenerIngresosRecientes();
}
