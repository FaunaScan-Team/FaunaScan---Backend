package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Notificacion;
import com.upc.faunascan.Repositories.NotificacionRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.NotificacionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public NotificacionDTO crear(NotificacionDTO dto) {
        Notificacion notificacion = modelMapper.map(dto, Notificacion.class);
        notificacion.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setLeido(false);
        return aDTO(notificacionRepository.save(notificacion));
    }

    public List<NotificacionDTO> listarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioOrderByFechaCreacionDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    public List<NotificacionDTO> listarNoLeidas(Long idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioAndLeidoFalse(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    public NotificacionDTO marcarLeida(Long idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con id: " + idNotificacion));
        notificacion.setLeido(true);
        return aDTO(notificacionRepository.save(notificacion));
    }

    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }

    private NotificacionDTO aDTO(Notificacion notificacion) {
        NotificacionDTO dto = modelMapper.map(notificacion, NotificacionDTO.class);
        if (notificacion.getUsuario() != null) {
            dto.setIdUsuario(notificacion.getUsuario().getIdUsuario());
        }
        return dto;
    }
}
