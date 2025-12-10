package utp.sistemalindley.repository;

import utp.sistemalindley.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByIdCliente(Integer idCliente);

    List<Pedido> findByEstadoPedido(String estadoPedido);

    @Query("SELECT p FROM Pedido p WHERE " +
            "(:idPedido IS NULL OR p.idPedido = :idPedido) AND " +
            "(:estadoPedido IS NULL OR p.estadoPedido = :estadoPedido) " +
            "ORDER BY p.fechaPedido DESC")
    List<Pedido> buscarPedidos(@Param("idPedido") Integer idPedido,
                               @Param("estadoPedido") String estadoPedido);
}
