package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.ContrasenaTemporalDTO;
import com.upc.faunascan.dto.RecuperarContrasenaDTO;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    // US01: crear cuenta
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO registrar(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return usuarioService.registrar(usuarioRegistroDTO);
    }

    // US03: recuperar contrasena
    @PostMapping("/recuperar-contrasena")
    public ContrasenaTemporalDTO recuperarContrasena(@RequestBody RecuperarContrasenaDTO recuperarContrasenaDTO) {
        String temporal = usuarioService.recuperarContrasena(recuperarContrasenaDTO.getCorreo());
        return new ContrasenaTemporalDTO(temporal);
    }

    // US04/US05: ver y editar perfil basico del usuario
    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizar(id, usuarioDTO);
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
