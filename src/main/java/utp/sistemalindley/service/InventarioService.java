package utp.sistemalindley.service;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Inventario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utp.sistemalindley.repository.InventarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    @Transactional
    public void actualizarInventario(Integer idProducto, Integer idAlmacen, Integer cantidad) {
        Inventario inventario = inventarioRepository
                .findByIdAlmacenAndIdProducto(idAlmacen, idProducto);

        if (inventario == null) {
            inventario = Inventario.builder()
                    .idAlmacen(idAlmacen)
                    .idProducto(idProducto)
                    .cantidadDisponible(cantidad)
                    .build();
        } else {
            inventario.setCantidadDisponible(
                    inventario.getCantidadDisponible() + cantidad
            );
        }

        inventarioRepository.save(inventario);
    }

    @Transactional
    public boolean reducirStock(Integer idProducto, Integer cantidad) {
        List<Inventario> inventarios = inventarioRepository
                .obtenerInventarioPorProducto(idProducto);

        int cantidadRestante = cantidad;

        for (Inventario inv : inventarios) {
            if (cantidadRestante <= 0) break;

            int disponible = inv.getCantidadDisponible();
            if (disponible > 0) {
                int aReducir = Math.min(disponible, cantidadRestante);
                inv.setCantidadDisponible(disponible - aReducir);
                inventarioRepository.save(inv);
                cantidadRestante -= aReducir;
            }
        }

        return cantidadRestante == 0;
    }
}
