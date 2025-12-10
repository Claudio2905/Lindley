package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Producto;
import org.springframework.stereotype.Service;
import utp.sistemalindley.repository.InventarioRepository;
import utp.sistemalindley.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findByEstado("Activo");
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarProductos(String nombre, String categoria) {
        if ((nombre == null || nombre.trim().isEmpty()) &&
                (categoria == null || categoria.trim().isEmpty())) {
            return listarTodos();
        }
        return productoRepository.buscarProductos(nombre, categoria);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Integer obtenerStockTotal(Integer idProducto) {
        return inventarioRepository.obtenerStockTotal(idProducto);
    }
}
