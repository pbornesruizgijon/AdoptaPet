package com.example.adoptapet.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.adoptapet.model.MascotaEntity;
import com.example.adoptapet.model.exceptions.AlreadyAdoptedException;
import com.example.adoptapet.service.PetService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PetController {

    private final PetService petService;

    // Inyección por constructor (Spring lo crea por ti)
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Página principal
    @GetMapping("/")
    public String index(Model model, @CookieValue(defaultValue = "") String alias, HttpSession session) {
        model.addAttribute("aliasCookie", alias == null ? "" : alias.trim());
        String usuarioSesion = (String) session.getAttribute("usuario");
        if (usuarioSesion != null) {
            model.addAttribute("usuario", usuarioSesion);
        }
        model.addAttribute("totalAdoptadas", petService.contarAdoptadas());
        return "index";
    }

    // Formulario de usuario
    @PostMapping("/setUsuario")
    public String setUsuario(@RequestParam String usuario, HttpSession session,
            HttpServletResponse response, Model model) {
        if (usuario == null || usuario.trim().length() < 2) {
            model.addAttribute("error", "Nombre inválido, mínimo 2 caracteres");
            model.addAttribute("totalAdoptadas", petService.contarAdoptadas());
            return "index";
        }
        session.setAttribute("usuario", usuario.trim());
        Cookie cookie = new Cookie("alias", usuario.trim());
        cookie.setMaxAge(24 * 3600);
        response.addCookie(cookie);
        return "redirect:/lista";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie cookie = new Cookie("alias", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        session.invalidate();
        return "redirect:/";
    }

    // Mostrar lista de mascotas (de BD)
    @GetMapping("/lista")
    public String lista(@RequestParam(required = false) String orden, Model model) {
        String campo = "nombre";
        if ("edad".equalsIgnoreCase(orden)) {
            campo = "edad";
        }
        List<MascotaEntity> mascotas = petService.ordenarPorCampo(campo);
        model.addAttribute("mascotas", mascotas);
        return "lista";
    }

    // Detalle de una mascota
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        MascotaEntity mascota = petService.findById(id).orElse(null);
        model.addAttribute("mascota", mascota);
        return "detalle";
    }

    // Adopción
    @PostMapping("/adoptar")
    public String adoptar(@RequestParam Long id, HttpSession session, Model model) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario == null || usuario.isBlank()) {
            model.addAttribute("error", "Debes identificarte antes de adoptar");
            return "index";
        }

        MascotaEntity mascota = petService.findById(id).orElse(null);
        if (mascota == null) {
            model.addAttribute("error", "Mascota no encontrada");
            return "detalle";
        }

        try {
            petService.adoptar(id, usuario);
            MascotaEntity mascotaActualizada = petService.findById(id).orElse(null);
            model.addAttribute("mascota", mascotaActualizada);
            model.addAttribute("mensaje", "¡Has adoptado correctamente!");
            return "detalle";
        } catch (AlreadyAdoptedException ex) {
            model.addAttribute("mascota", mascota);
            model.addAttribute("error", ex.getMessage());
            return "detalle";
        }
    }
}
