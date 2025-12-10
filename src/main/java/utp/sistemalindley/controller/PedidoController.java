package utp.sistemalindley.controller;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.DetallePedido;
import utp.sistemalindley.model.Pedido;
import utp.sistemalindley.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utp.sistemalindley.service.ClienteService;
import utp.sistemalindley.service.PedidoService;
import utp.sistemalindley.service.ProductoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@SessionAttributes("carrito")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ClienteService clienteService;

    @ModelAttribute("carrito")
    public List<ItemCarrito> carrito() {
        return new ArrayList<>();
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("clientes", clienteService.listarActivos());
        model.addAttribute("productos", productoService.listarTodos());
        return "pedidos/realizar";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Integer idProducto,
                                   @RequestParam Integer cantidad,
                                   @ModelAttribute("carrito") List<ItemCarrito> carrito,
                                   RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Integer stockDisponible = productoService.obtenerStockTotal(idProducto);
            if (stockDisponible < cantidad) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "Stock insuficiente. Disponible: " + stockDisponible);
                redirectAttributes.addFlashAttribute("tipo", "warning");
                return "redirect:/pedidos/nuevo";
            }

            ItemCarrito item = new ItemCarrito();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setSubtotal(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));

            carrito.add(item);

            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/pedidos/nuevo";
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(@RequestParam(required = false) Integer idCliente,
                                  @ModelAttribute("carrito") List<ItemCarrito> carrito,
                                  RedirectAttributes redirectAttributes,
                                  org.springframework.web.bind.support.SessionStatus status,
                                  @RequestParam(required = false) java.util.Map<String, String> allParams) {
        try {
            System.out.println("=== CONFIRMAR PEDIDO ===");
            System.out.println("ID Cliente recibido: " + idCliente);
            System.out.println("Carrito size: " + (carrito != null ? carrito.size() : "null"));
            System.out.println("Todos los parámetros: " + allParams);

            // Validar que se haya seleccionado un cliente
            if (idCliente == null) {
                System.out.println("ERROR: idCliente es null");
                redirectAttributes.addFlashAttribute("mensaje", "Debe seleccionar un cliente");
                redirectAttributes.addFlashAttribute("tipo", "warning");
                return "redirect:/pedidos/nuevo";
            }

            // Validar que el carrito no esté vacío
            if (carrito == null || carrito.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El carrito está vacío");
                redirectAttributes.addFlashAttribute("tipo", "warning");
                return "redirect:/pedidos/nuevo";
            }

            // Calcular total
            BigDecimal total = carrito.stream()
                    .map(ItemCarrito::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Crear pedido
            Pedido pedido = Pedido.builder()
                    .idCliente(idCliente)
                    .total(total)
                    .estadoPedido("Pendiente")
                    .build();

            // Crear detalles
            List<DetallePedido> detalles = new ArrayList<>();
            for (ItemCarrito item : carrito) {
                DetallePedido detalle = DetallePedido.builder()
                        .idProducto(item.getProducto().getIdProducto())
                        .cantidad(item.getCantidad())
                        .subtotal(item.getSubtotal())
                        .build();
                detalles.add(detalle);
            }

            // Guardar pedido
            Pedido pedidoGuardado = pedidoService.crearPedido(pedido, detalles);

            // Limpiar carrito y sesión
            carrito.clear();
            status.setComplete();

            redirectAttributes.addFlashAttribute("mensaje",
                    "Pedido confirmado exitosamente. Código: " + pedidoGuardado.getIdPedido());
            redirectAttributes.addFlashAttribute("tipo", "success");

            return "redirect:/pedidos/nuevo";

        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en consola
            redirectAttributes.addFlashAttribute("mensaje", "Error al confirmar pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/pedidos/nuevo";
        }
    }

    @PostMapping("/limpiar")
    public String limpiarCarrito(@ModelAttribute("carrito") List<ItemCarrito> carrito,
                                 org.springframework.web.bind.support.SessionStatus status,
                                 RedirectAttributes redirectAttributes) {
        carrito.clear();
        status.setComplete();
        redirectAttributes.addFlashAttribute("mensaje", "Carrito limpiado");
        redirectAttributes.addFlashAttribute("tipo", "info");
        return "redirect:/pedidos/nuevo";
    }

    @GetMapping("/consultar")
    public String consultarOrdenes(Model model,
                                   @RequestParam(required = false) Integer codigo,
                                   @RequestParam(required = false) String estado) {
        List<Pedido> pedidos = pedidoService.buscarPedidos(codigo, estado);
        model.addAttribute("pedidos", pedidos);
        return "pedidos/consultar";
    }

    // Clase auxiliar
    @lombok.Data
    public static class ItemCarrito {
        private Producto producto;
        private Integer cantidad;
        private BigDecimal subtotal;
    }
}