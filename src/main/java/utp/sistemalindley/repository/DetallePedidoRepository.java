package utp.sistemalindley.repository;

import utp.sistemalindley.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByIdPedido(Integer idPedido);

    void deleteByIdPedido(Integer idPedido);
}
