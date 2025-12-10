package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Cliente;
import org.springframework.stereotype.Service;
import utp.sistemalindley.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado("Activo");
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreCliente(nombre);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
