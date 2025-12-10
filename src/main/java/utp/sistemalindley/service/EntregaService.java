package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Entrega;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utp.sistemalindley.repository.EntregaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntregaService {

    private final EntregaRepository entregaRepository;

    public List<Entrega> listarPendientes() {
        return entregaRepository.obtenerEntregasPendientes();
    }

    public List<Entrega> listarPorEstado(String estado) {
        return entregaRepository.findByEstadoEntrega(estado);
    }

    public Optional<Entrega> buscarPorId(Integer id) {
        return entregaRepository.findById(id);
    }

    public List<Entrega> buscarPorPedido(Integer idPedido) {
        return entregaRepository.findByIdPedido(idPedido);
    }

    @Transactional
    public Entrega programarEntrega(Entrega entrega) {
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega actualizarEstado(Integer idEntrega, String nuevoEstado) {
        Optional<Entrega> entregaOpt = entregaRepository.findById(idEntrega);
        if (entregaOpt.isPresent()) {
            Entrega entrega = entregaOpt.get();
            entrega.setEstadoEntrega(nuevoEstado);

            if ("Completada".equals(nuevoEstado) || "Recibido".equals(nuevoEstado)) {
                entrega.setFechaLlegada(LocalDateTime.now());
            }

            return entregaRepository.save(entrega);
        }
        return null;
    }
}
