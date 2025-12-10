package utp.sistemalindley.repository;

import utp.sistemalindley.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByEstado(String estado);

    Cliente findByNombreCliente(String nombreCliente);

    List<Cliente> findByNombreClienteContainingIgnoreCaseAndEstado(String nombre, String estado);
}
