package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.AvistamientoCompartido;
import com.upc.faunascan.Repositories.AvistamientoCompartidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvistamientoCompartidoService {

    private final AvistamientoCompartidoRepository avistamientoCompartidoRepository;

    // US48: compartir el link de un avistamiento con otro investigador
    public AvistamientoCompartido compartir(AvistamientoCompartido compartido) {
        compartido.setFechaCompartido(LocalDateTime.now());
        return avistamientoCompartidoRepository.save(compartido);
    }

    public List<AvistamientoCompartido> listarRecibidos(Long idUsuario) {
        return avistamientoCompartidoRepository.findByInvestigadorDestino_IdUsuarioOrderByFechaCompartidoDesc(idUsuario);
    }

    public List<AvistamientoCompartido> listarEnviados(Long idUsuario) {
        return avistamientoCompartidoRepository.findByInvestigadorOrigen_IdUsuarioOrderByFechaCompartidoDesc(idUsuario);
    }

    public void eliminar(Long id) {
        avistamientoCompartidoRepository.deleteById(id);
    }
}
