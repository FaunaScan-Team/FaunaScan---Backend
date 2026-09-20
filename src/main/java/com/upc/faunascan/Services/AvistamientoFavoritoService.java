package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.AvistamientoFavorito;
import com.upc.faunascan.Repositories.AvistamientoFavoritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoFavoritoService {

    private final AvistamientoFavoritoRepository avistamientoFavoritoRepository;

    // US26: marcar un avistamiento como favorito
    public AvistamientoFavorito marcar(AvistamientoFavorito favorito) {
        boolean yaExiste = avistamientoFavoritoRepository.existsByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(
                favorito.getUsuario().getIdUsuario(),
                favorito.getAvistamiento().getIdAvistamiento());

        if (yaExiste) {
            throw new RuntimeException("Este avistamiento ya esta marcado como favorito");
        }
        favorito.setFechaMarcado(LocalDateTime.now());
        return avistamientoFavoritoRepository.save(favorito);
    }

    public List<AvistamientoFavorito> listarPorUsuario(Long idUsuario) {
        return avistamientoFavoritoRepository.findByUsuario_IdUsuario(idUsuario);
    }

    // US49: quitar de favoritos
    public void desmarcar(Long idUsuario, Long idAvistamiento) {
        AvistamientoFavorito favorito = avistamientoFavoritoRepository
                .findByUsuario_IdUsuarioAndAvistamiento_IdAvistamiento(idUsuario, idAvistamiento)
                .orElseThrow(() -> new RuntimeException("Este avistamiento no esta en tus favoritos"));
        avistamientoFavoritoRepository.delete(favorito);
    }
}
