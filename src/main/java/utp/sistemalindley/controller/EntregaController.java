package utp.sistemalindley.controller;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Entrega;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utp.sistemalindley.service.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;
    private final EmpleadoService empleadoService;
    private final VehiculoService vehiculoService;
    private final RutaDistribucionService rutaService;
    private final PedidoService pedidoService;

    @GetMapping("/gestionar")
    public String gestionarEntregas(Model model) {
        model.addAttribute("pedidosPendientes", pedidoService.buscarPedidos(null, "Pendiente"));
        model.addAttribute("transportistas", empleadoService.listarTransportistas());
        model.addAttribute("vehiculos", vehiculoService.listarDisponibles());
        model.addAttribute("rutas", rutaService.listarActivas());
        model.addAttribute("entregas", entregaService.listarPendientes());
        return "entregas/gestionar";
    }

    @PostMapping("/asignar")
    public String asignarEntrega(@RequestParam Integer idPedido,
                                 @RequestParam Integer idEmpleado,
                                 @RequestParam Integer idVehiculo,
                                 @RequestParam Integer idRuta,
                                 RedirectAttributes redirectAttributes) {
        try {
            Entrega entrega = Entrega.builder()
                    .idPedido(idPedido)
                    .idEmpleado(idEmpleado)
                    .idVehiculo(idVehiculo)
                    .idRuta(idRuta)
                    .estadoEntrega("Programada")
                    .fechaSalida(LocalDateTime.now()) // Asignar fecha de salida
                    .build();

            entregaService.programarEntrega(entrega);
            pedidoService.actualizarEstado(idPedido, "En Proceso");

            redirectAttributes.addFlashAttribute("mensaje", "Entrega programada correctamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/entregas/gestionar";
    }

    @GetMapping("/confirmar")
    public String confirmarRecepcion(Model model) {
        // CAMBIO: Buscar entregas "Completada" en lugar de "En Tránsito"
        // porque las entregas pasan de "Programada" a "Completada" directamente
        model.addAttribute("entregas", entregaService.listarPorEstado("Completada"));
        return "entregas/confirmar";
    }

    @PostMapping("/confirmar/{id}")
    public String confirmarRecepcion(@PathVariable Integer id,
                                     RedirectAttributes redirectAttributes) {
        try {
            entregaService.actualizarEstado(id, "Recibido");

            // Obtener la entrega y actualizar el estado del pedido
            Entrega entrega = entregaService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
            pedidoService.actualizarEstado(entrega.getIdPedido(), "Completado");

            redirectAttributes.addFlashAttribute("mensaje", "Recepción confirmada");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/entregas/confirmar";
    }

    @GetMapping("/entregar")
    public String entregarProductos(Model model) {
        model.addAttribute("entregas", entregaService.listarPorEstado("Programada"));
        return "entregas/entregar";
    }

    @PostMapping("/completar/{id}")
    public String completarEntrega(@PathVariable Integer id,
                                   @RequestParam(required = false) String observaciones,
                                   RedirectAttributes redirectAttributes) {
        try {
            Entrega entrega = entregaService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

            entrega.setEstadoEntrega("Completada");
            entrega.setObservaciones(observaciones);
            entrega.setFechaLlegada(LocalDateTime.now()); // Asignar fecha de llegada
            entregaService.programarEntrega(entrega);

            redirectAttributes.addFlashAttribute("mensaje", "Entrega completada correctamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/entregas/entregar";
    }
}