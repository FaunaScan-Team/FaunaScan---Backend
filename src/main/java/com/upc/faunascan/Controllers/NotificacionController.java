package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.NotificacionService;
import com.upc.faunascan.dto.AvisoSistemaDTO;
import com.upc.faunascan.dto.EnvioNotificacionesDTO;
import com.upc.faunascan.dto.NotificacionCriticaDTO;
import com.upc.faunascan.dto.NotificacionDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService notificacionService;

    @Operation(summary = "Crear una notificación (administrador)")
    @PostMapping("/notificaciones")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificacionDTO crear(@Valid @RequestBody NotificacionDTO notificacionDTO) {
        return notificacionService.crear(notificacionDTO);
    }

    // HU-34: alerta critica a todos los usuarios por un avistamiento validado de especie vulnerable
    // (tambien se dispara sola al validar el avistamiento)
    @Operation(summary = "Enviar alerta crítica por especie vulnerable")
    @PostMapping("/notificaciones/critica")
    @PreAuthorize("hasAnyRole('INVESTIGADOR', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioNotificacionesDTO enviarCritica(@Valid @RequestBody NotificacionCriticaDTO criticaDTO) {
        int enviadas = notificacionService.notificarEspecieVulnerable(criticaDTO.getIdAvistamiento());
        return new EnvioNotificacionesDTO("Crítica", enviadas);
    }

    // HU-12/HU-40: aviso de sistema o mantenimiento para todos los usuarios activos
    @Operation(summary = "Enviar aviso de mantenimiento del sistema")
    @PostMapping("/notificaciones/mantenimiento")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EnvioNotificacionesDTO enviarAvisoSistema(@Valid @RequestBody AvisoSistemaDTO avisoDTO) {
        return new EnvioNotificacionesDTO("Sistema", notificacionService.enviarAvisoSistema(avisoDTO.getMensaje()));
    }

    // HU-59: ejecuta a mano la tarea programada de recordatorios (corre sola todos los dias)
    @Operation(summary = "Ejecutar recordatorios automáticos")
    @PostMapping("/notificaciones/recordatorios")
    @PreAuthorize("hasRole('ADMIN')")
    public EnvioNotificacionesDTO enviarRecordatorios() {
        return new EnvioNotificacionesDTO("Alerta", notificacionService.enviarRecordatorios());
    }

    // HU-11/HU-12/HU-13/HU-59: notificaciones del usuario, filtradas por categoria (critica, sistema, alerta)
    @Operation(summary = "Listar notificaciones del usuario por categoría")
    @GetMapping("/usuarios/{idUsuario}/notificaciones")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<NotificacionDTO> listarPorUsuario(@PathVariable Long idUsuario,
                                                  @RequestParam(required = false) String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return notificacionService.listarPorUsuario(idUsuario);
        }
        return notificacionService.listarPorUsuarioYCategoria(idUsuario, categoria);
    }

    @Operation(summary = "Listar notificaciones no leídas del usuario")
    @GetMapping("/usuarios/{idUsuario}/notificaciones/no-leidas")
    @PreAuthorize("@autorizacion.esUsuario(#idUsuario) or hasRole('ADMIN')")
    public List<NotificacionDTO> listarNoLeidas(@PathVariable Long idUsuario) {
        return notificacionService.listarNoLeidas(idUsuario);
    }

    @Operation(summary = "Marcar una notificación como leída")
    @PatchMapping("/notificaciones/{id}/leer")
    @PreAuthorize("@autorizacion.esDuenoNotificacion(#id) or hasRole('ADMIN')")
    public NotificacionDTO marcarLeida(@PathVariable Long id) {
        return notificacionService.marcarLeida(id);
    }

    @Operation(summary = "Eliminar una notificación")
    @DeleteMapping("/notificaciones/{id}")
    @PreAuthorize("@autorizacion.esDuenoNotificacion(#id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
    }
}
