package utp.sistemalindley.controller;

import lombok.RequiredArgsConstructor;
import utp.sistemalindley.model.IngresoProducto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utp.sistemalindley.service.IngresoProductoService;
import utp.sistemalindley.service.ProductoService;

@Controller
@RequestMapping("/ingresos")
@RequiredArgsConstructor
public class IngresoProductoController {

    private final IngresoProductoService ingresoService;
    private final ProductoService productoService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("ingresos", ingresoService.listarRecientes());
        model.addAttribute("ingreso", new IngresoProducto());
        return "ingresos/registrar";
    }

    @PostMapping("/registrar")
    public String registrarIngreso(@ModelAttribute IngresoProducto ingreso,
                                   RedirectAttributes redirectAttributes) {
        try {
            ingreso.setIdAlmacen(1); // Almacén por defecto
            ingresoService.registrarIngreso(ingreso);
            redirectAttributes.addFlashAttribute("mensaje", "Ingreso registrado correctamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar ingreso: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
        }
        return "redirect:/ingresos";
    }
}
