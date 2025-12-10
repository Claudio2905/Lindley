package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.RutaDistribucion;
import org.springframework.stereotype.Service;
import utp.sistemalindley.repository.RutaDistribucionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RutaDistribucionService {

    private final RutaDistribucionRepository rutaRepository;

    public List<RutaDistribucion> listarActivas() {
        return rutaRepository.findByEstado("Activa");
    }

    public Optional<RutaDistribucion> buscarPorId(Integer id) {
        return rutaRepository.findById(id);
    }
}
