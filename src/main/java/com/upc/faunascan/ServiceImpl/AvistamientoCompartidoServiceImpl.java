package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.AvistamientoCompartidoService;
import com.upc.faunascan.Entities.AvistamientoCompartido;
import com.upc.faunascan.Repositories.AvistamientoCompartidoRepository;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.AvistamientoCompartidoDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

    // HU-56: compartir el link de un avistamiento con otro investigador
@Service
@RequiredArgsConstructor
public class AvistamientoCompartidoServiceImpl implements AvistamientoCompartidoService {
    private final AvistamientoCompartidoRepository avistamientoCompartidoRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public AvistamientoCompartidoDTO compartir(AvistamientoCompartidoDTO dto) {
        AvistamientoCompartido compartido = modelMapper.map(dto, AvistamientoCompartido.class);
        compartido.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        compartido.setInvestigadorOrigen(usuarioRepository.findById(dto.getIdInvestigadorOrigen())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + dto.getIdInvestigadorOrigen())));
        compartido.setInvestigadorDestino(usuarioRepository.findById(dto.getIdInvestigadorDestino())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + dto.getIdInvestigadorDestino())));
        compartido.setFechaCompartido(LocalDateTime.now());
        return aDTO(avistamientoCompartidoRepository.save(compartido));
    }

    @Override
    public List<AvistamientoCompartidoDTO> listarRecibidos(Long idUsuario) {
        return avistamientoCompartidoRepository
                .findByInvestigadorDestino_IdUsuarioOrderByFechaCompartidoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    @Override
    public List<AvistamientoCompartidoDTO> listarEnviados(Long idUsuario) {
        return avistamientoCompartidoRepository
                .findByInvestigadorOrigen_IdUsuarioOrderByFechaCompartidoDesc(idUsuario)
                .stream().map(this::aDTO).toList();
    }

    @Override
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
