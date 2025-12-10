package utp.sistemalindley.controller;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utp.sistemalindley.service.ProductoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final ProductoService productoService;

    @GetMapping
    public String mostrarConsulta(Model model,
                                  @RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria) {

        List<ProductoDisponibilidad> productos = new ArrayList<>();
        List<Producto> listaProductos;

        if (nombre != null || categoria != null) {
            listaProductos = productoService.buscarProductos(nombre, categoria);
        } else {
            listaProductos = productoService.listarTodos();
        }

        for (Producto p : listaProductos) {
            ProductoDisponibilidad pd = new ProductoDisponibilidad();
            pd.setProducto(p);
            pd.setStockDisponible(productoService.obtenerStockTotal(p.getIdProducto()));
            productos.add(pd);
        }

        model.addAttribute("productos", productos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        return "disponibilidad/consultar";
    }

    // Clase auxiliar
    @lombok.Data
    public static class ProductoDisponibilidad {
        private Producto producto;
        private Integer stockDisponible;
    }
}