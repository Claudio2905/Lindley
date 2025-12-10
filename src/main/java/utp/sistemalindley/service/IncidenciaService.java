package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Incidencia;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utp.sistemalindley.repository.IncidenciaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;

    public List<Incidencia> listarRecientes() {
        return incidenciaRepository.obtenerIncidenciasRecientes();
    }

    public Optional<Incidencia> buscarPorId(Integer id) {
        return incidenciaRepository.findById(id);
    }

    @Transactional
    public Incidencia registrar(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }
}
