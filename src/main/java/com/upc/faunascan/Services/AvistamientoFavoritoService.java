package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.AvistamientoFavorito;
import com.upc.faunascan.Repositories.AvistamientoFavoritoRepository;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.AvistamientoFavoritoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// US26/US49: marcar y quitar avistamientos favoritos
@Service
@RequiredArgsConstructor
public class AvistamientoFavoritoService {

    private final AvistamientoFavoritoRepository avistamientoFavoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ModelMapper modelMapper;

    public AvistamientoFavoritoDTO marcar(AvistamientoFavoritoDTO dto) {
        boolean yaExiste = avistamientoFavoritoRepository
                .existsByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(dto.getIdUsuario(), dto.getIdAvistamiento());
        if (yaExiste) {
            throw new RuntimeException("Este avistamiento ya esta marcado como favorito");
        }

        AvistamientoFavorito favorito = new AvistamientoFavorito();
        favorito.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        favorito.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RuntimeException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        favorito.setFechaMarcado(LocalDateTime.now());
        return aDTO(avistamientoFavoritoRepository.save(favorito));
    }

    public List<AvistamientoFavoritoDTO> listarPorUsuario(Long idUsuario) {
        return avistamientoFavoritoRepository.findByUsuario_IdUsuario(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    public void desmarcar(Long idUsuario, Long idAvistamiento) {
        AvistamientoFavorito favorito = avistamientoFavoritoRepository
                .findByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(idUsuario, idAvistamiento)
                .orElseThrow(() -> new RuntimeException("Este avistamiento no esta en tus favoritos"));
        avistamientoFavoritoRepository.delete(favorito);
    }

    private AvistamientoFavoritoDTO aDTO(AvistamientoFavorito favorito) {
        AvistamientoFavoritoDTO dto = modelMapper.map(favorito, AvistamientoFavoritoDTO.class);
        if (favorito.getUsuario() != null) {
            dto.setIdUsuario(favorito.getUsuario().getIdUsuario());
        }
        if (favorito.getAvistamiento() != null) {
            dto.setIdAvistamiento(favorito.getAvistamiento().getIdAvistamiento());
        }
        return dto;
    }
}
