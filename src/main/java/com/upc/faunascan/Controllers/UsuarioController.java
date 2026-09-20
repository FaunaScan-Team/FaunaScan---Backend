package com.upc.faunascan.Controllers;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Services.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    // US01: crear cuenta
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrar(usuario);
    }

    // US02: iniciar sesion
    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return usuarioService.iniciarSesion(request.getCorreo(), request.getContrasena());
    }

    // US03: recuperar contrasena
    @PostMapping("/recuperar-contrasena")
    public Map<String, String> recuperarContrasena(@RequestBody Map<String, String> body) {
        String temporal = usuarioService.recuperarContrasena(body.get("correo"));
        return Map.of("contrasenaTemporal", temporal);
    }

    // US04/US05: ver y editar perfil basico del usuario
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @PatchMapping("/{id}/estado")
    public Usuario cambiarEstado(@PathVariable Long id, @RequestParam boolean estado) {
        return usuarioService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String correo;
        private String contrasena;
    }
}
