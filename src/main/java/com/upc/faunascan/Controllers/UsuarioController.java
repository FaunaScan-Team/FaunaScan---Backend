package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.AvistamientoService;
import com.upc.faunascan.Services.PerfilService;
import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final AvistamientoService avistamientoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    // HU-04: visualizacion de perfil del usuario autenticado
    @GetMapping("/perfil")
    public PerfilUsuarioDTO verPerfil(Authentication authentication) {
        return perfilService.obtenerPerfil(authentication.getName());
    }

    // HU-05: edicion de perfil del usuario autenticado
    @PutMapping("/perfil")
    public PerfilUsuarioDTO editarPerfil(Authentication authentication, @Valid @RequestBody PerfilUsuarioDTO perfilDTO) {
        return perfilService.actualizarPerfil(authentication.getName(), perfilDTO);
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuario(#id) or hasRole('ADMIN')")
    public UsuarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizar(id, usuarioDTO);
    }

    // HU-23: resumen de progreso (avistamientos, especies y zonas)
    @GetMapping("/{idUsuario}/progreso")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public ProgresoDTO progreso(@PathVariable Long idUsuario) {
        return avistamientoService.calcularProgreso(idUsuario);
    }

    // HU-54: resumen de contribucion
    @GetMapping("/{idUsuario}/contribucion")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public ContribucionDTO contribucion(@PathVariable Long idUsuario) {
        return avistamientoService.calcularContribucion(idUsuario);
    }

    // HU-31: el investigador registra su credencial institucional
    @PostMapping("/{idUsuario}/credencial")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario)")
    public UsuarioDTO registrarCredencial(@PathVariable Long idUsuario,
                                         @Valid @RequestBody CredencialDTO credencialDTO) {
        return usuarioService.registrarCredencial(idUsuario, credencialDTO.getUrlCredencial());
    }

    // HU-32: consultar preferencias de notificaciones
    @GetMapping("/{idUsuario}/preferencias-notificaciones")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public PreferenciasNotificacionesDTO verPreferencias(@PathVariable Long idUsuario) {
        return usuarioService.obtenerPreferencias(idUsuario);
    }

    // HU-32: configurar preferencias de notificaciones
    @PutMapping("/{idUsuario}/preferencias-notificaciones")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public PreferenciasNotificacionesDTO actualizarPreferencias(@PathVariable Long idUsuario,
                                                                @RequestBody PreferenciasNotificacionesDTO preferenciasDTO) {
        return usuarioService.actualizarPreferencias(idUsuario, preferenciasDTO);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioDTO cambiarEstado(@PathVariable Long id, @RequestParam boolean estado) {
        return usuarioService.cambiarEstado(id, estado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
