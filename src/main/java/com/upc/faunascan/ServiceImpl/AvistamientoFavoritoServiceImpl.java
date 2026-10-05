package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.AvistamientoFavoritoService;
import com.upc.faunascan.Entities.AvistamientoFavorito;
import com.upc.faunascan.Repositories.AvistamientoFavoritoRepository;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.AvistamientoFavoritoDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoFavoritoServiceImpl implements AvistamientoFavoritoService {
    private final AvistamientoFavoritoRepository avistamientoFavoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ModelMapper modelMapper;

    @Override
    public AvistamientoFavoritoDTO marcar(AvistamientoFavoritoDTO dto) {
        boolean yaExiste = avistamientoFavoritoRepository
                .existsByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(dto.getIdUsuario(), dto.getIdAvistamiento());
        if (yaExiste) {
            throw new RuntimeException("Este avistamiento ya esta marcado como favorito");
        }

        AvistamientoFavorito favorito = new AvistamientoFavorito();
        favorito.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + dto.getIdUsuario())));
        favorito.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        favorito.setFechaMarcado(LocalDateTime.now());
        return aDTO(avistamientoFavoritoRepository.save(favorito));
    }

    @Override
    public List<AvistamientoFavoritoDTO> listarPorUsuario(Long idUsuario) {
        return avistamientoFavoritoRepository.findByUsuario_IdUsuario(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    @Override
    public void desmarcar(Long idUsuario, Long idAvistamiento) {
        AvistamientoFavorito favorito = avistamientoFavoritoRepository
                .findByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(idUsuario, idAvistamiento)
                .orElseThrow(() -> new RecursoNoEncontradoException("Este avistamiento no esta en tus favoritos"));
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
