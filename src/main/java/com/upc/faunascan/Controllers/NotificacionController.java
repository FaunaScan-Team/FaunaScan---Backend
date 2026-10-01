package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.NotificacionService;
import com.upc.faunascan.dto.NotificacionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService notificacionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificacionDTO crear(@RequestBody NotificacionDTO notificacionDTO) {
        return notificacionService.crear(notificacionDTO);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<NotificacionDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return notificacionService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/no-leidas")
    public List<NotificacionDTO> listarNoLeidas(@PathVariable Long idUsuario) {
        return notificacionService.listarNoLeidas(idUsuario);
    }

    @PatchMapping("/{id}/leer")
    public NotificacionDTO marcarLeida(@PathVariable Long id) {
        return notificacionService.marcarLeida(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
    }
}
