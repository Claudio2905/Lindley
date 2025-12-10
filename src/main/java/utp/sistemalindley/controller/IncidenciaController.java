package utp.sistemalindley.controller;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.Entrega;
import utp.sistemalindley.model.Incidencia;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utp.sistemalindley.service.EntregaService;
import utp.sistemalindley.service.IncidenciaService;

import java.util.List;

@Controller
@RequestMapping("/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final EntregaService entregaService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("incidencias", incidenciaService.listarRecientes());
        model.addAttribute("incidencia", new Incidencia());
        return "incidencias/reportar";
    }

    @PostMapping("/reportar")
    public String reportarIncidencia(@RequestParam Integer idPedido,
                                     @RequestParam String tipoIncidencia,
                                     @RequestParam String descripcion,
                                     RedirectAttributes redirectAttributes) {
        try {
            List<Entrega> entregas = entregaService.buscarPorPedido(idPedido);
            if (entregas.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "No se encontró entrega para el pedido " + idPedido);
                redirectAttributes.addFlashAttribute("tipo", "warning");
                return "redirect:/incidencias";
            }

            Incidencia incidencia = Incidencia.builder()
                    .idEntrega(entregas.get(0).getIdEntrega())
                    .tipoIncidencia(tipoIncidencia)
                    .descripcion(descripcion)
                    .estado("Pendiente")
                    .build();

            incidenciaService.registrar(incidencia);

            redirectAttributes.addFlashAttribute("mensaje", "Incidencia registrada correctamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/incidencias";
    }
}