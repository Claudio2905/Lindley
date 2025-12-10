package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.IngresoProducto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utp.sistemalindley.repository.IngresoProductoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngresoProductoService {

    private final IngresoProductoRepository ingresoRepository;
    private final InventarioService inventarioService;

    public List<IngresoProducto> listarRecientes() {
        return ingresoRepository.obtenerIngresosRecientes();
    }

    @Transactional
    public IngresoProducto registrarIngreso(IngresoProducto ingreso) {
        // Guardar ingreso
        IngresoProducto ingresoGuardado = ingresoRepository.save(ingreso);

        // Actualizar inventario
        inventarioService.actualizarInventario(
                ingreso.getIdProducto(),
                ingreso.getIdAlmacen(),
                ingreso.getCantidad()
        );

        return ingresoGuardado;
    }
}
