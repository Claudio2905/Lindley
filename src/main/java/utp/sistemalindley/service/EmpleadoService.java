package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Empleado;
import org.springframework.stereotype.Service;
import utp.sistemalindley.repository.EmpleadoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public List<Empleado> listarTransportistas() {
        return empleadoRepository.obtenerTransportistasDisponibles();
    }

    public List<Empleado> listarActivos() {
        return empleadoRepository.findByEstado("Activo");
    }

    public Optional<Empleado> buscarPorId(Integer id) {
        return empleadoRepository.findById(id);
    }
}
