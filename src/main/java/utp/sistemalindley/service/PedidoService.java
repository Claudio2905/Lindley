package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.DetallePedido;
import utp.sistemalindley.model.Pedido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utp.sistemalindley.repository.DetallePedidoRepository;
import utp.sistemalindley.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final InventarioService inventarioService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPedidos(Integer idPedido, String estadoPedido) {
        return pedidoRepository.buscarPedidos(idPedido, estadoPedido);
    }

    public List<DetallePedido> obtenerDetalles(Integer idPedido) {
        return detallePedidoRepository.findByIdPedido(idPedido);
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido, List<DetallePedido> detalles) {
        // Guardar pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Guardar detalles
        for (DetallePedido detalle : detalles) {
            detalle.setIdPedido(pedidoGuardado.getIdPedido());
            detallePedidoRepository.save(detalle);

            // Reducir stock
            inventarioService.reducirStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        return pedidoGuardado;
    }

    @Transactional
    public Pedido actualizarEstado(Integer idPedido, String nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstadoPedido(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }
}
