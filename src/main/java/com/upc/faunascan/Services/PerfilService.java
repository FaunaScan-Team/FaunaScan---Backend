package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Perfil;
import com.upc.faunascan.Repositories.PerfilRepository;
import com.upc.faunascan.dto.PerfilDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final ModelMapper modelMapper;

    public PerfilDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    public PerfilDTO crear(PerfilDTO dto) {
        Perfil perfil = modelMapper.map(dto, Perfil.class);
        perfil.setFechaActualizacion(LocalDateTime.now());
        return aDTO(perfilRepository.save(perfil));
    }

    // US05: editar perfil (biografia, ubicacion, foto se maneja en Usuario.urlCredencial)
    public PerfilDTO actualizar(Long id, PerfilDTO dto) {
        Perfil perfil = buscarEntidad(id);
        perfil.setBiografia(dto.getBiografia());
        perfil.setUbicacion(dto.getUbicacion());
        perfil.setFechaActualizacion(LocalDateTime.now());
        return aDTO(perfilRepository.save(perfil));
    }

    public void eliminar(Long id) {
        perfilRepository.delete(buscarEntidad(id));
    }

    public Perfil buscarEntidad(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con id: " + id));
    }

    private PerfilDTO aDTO(Perfil perfil) {
        PerfilDTO dto = modelMapper.map(perfil, PerfilDTO.class);
        if (perfil.getUsuario() != null) {
            dto.setIdUsuario(perfil.getUsuario().getIdUsuario());
        }
        return dto;
    }
}
