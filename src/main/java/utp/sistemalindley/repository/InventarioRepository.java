package utp.sistemalindley.repository;

import utp.sistemalindley.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    List<Inventario> findByIdAlmacen(Integer idAlmacen);

    List<Inventario> findByIdProducto(Integer idProducto);

    Inventario findByIdAlmacenAndIdProducto(Integer idAlmacen, Integer idProducto);

    @Query("SELECT i FROM Inventario i WHERE i.idProducto = :idProducto")
    List<Inventario> obtenerInventarioPorProducto(@Param("idProducto") Integer idProducto);

    @Query("SELECT COALESCE(SUM(i.cantidadDisponible), 0) FROM Inventario i WHERE i.idProducto = :idProducto")
    Integer obtenerStockTotal(@Param("idProducto") Integer idProducto);
}
