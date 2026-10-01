package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.AvistamientoCompartido;
import com.upc.faunascan.Repositories.AvistamientoCompartidoRepository;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.AvistamientoCompartidoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// US48: compartir un avistamiento entre investigadores
@Service
@RequiredArgsConstructor
public class AvistamientoCompartidoService {

    private final AvistamientoCompartidoRepository avistamientoCompartidoRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public AvistamientoCompartidoDTO compartir(AvistamientoCompartidoDTO dto) {
        AvistamientoCompartido compartido = modelMapper.map(dto, AvistamientoCompartido.class);
        compartido.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RuntimeException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        compartido.setInvestigadorOrigen(usuarioRepository.findById(dto.getIdInvestigadorOrigen())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con id: " + dto.getIdInvestigadorOrigen())));
        compartido.setInvestigadorDestino(usuarioRepository.findById(dto.getIdInvestigadorDestino())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con id: " + dto.getIdInvestigadorDestino())));
        compartido.setFechaCompartido(LocalDateTime.now());
        return aDTO(avistamientoCompartidoRepository.save(compartido));
    }

    public List<AvistamientoCompartidoDTO> listarRecibidos(Long idUsuario) {
        return avistamientoCompartidoRepository
                .findByInvestigadorDestino_IdUsuarioOrderByFechaCompartidoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    public List<AvistamientoCompartidoDTO> listarEnviados(Long idUsuario) {
        return avistamientoCompartidoRepository
                .findByInvestigadorOrigen_IdUsuarioOrderByFechaCompartidoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    public void eliminar(Long id) {
        avistamientoCompartidoRepository.deleteById(id);
    }

    private AvistamientoCompartidoDTO aDTO(AvistamientoCompartido compartido) {
        AvistamientoCompartidoDTO dto = modelMapper.map(compartido, AvistamientoCompartidoDTO.class);
        if (compartido.getAvistamiento() != null) {
            dto.setIdAvistamiento(compartido.getAvistamiento().getIdAvistamiento());
        }
        if (compartido.getInvestigadorOrigen() != null) {
            dto.setIdInvestigadorOrigen(compartido.getInvestigadorOrigen().getIdUsuario());
        }
        if (compartido.getInvestigadorDestino() != null) {
            dto.setIdInvestigadorDestino(compartido.getInvestigadorDestino().getIdUsuario());
        }
        return dto;
    }
}
