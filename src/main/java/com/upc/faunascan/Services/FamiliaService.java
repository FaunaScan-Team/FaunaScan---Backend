package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Familia;
import com.upc.faunascan.Repositories.FamiliaRepository;
import com.upc.faunascan.dto.FamiliaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamiliaService {

    private final FamiliaRepository familiaRepository;
    private final ModelMapper modelMapper;

    public List<FamiliaDTO> listar() {
        return familiaRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, FamiliaDTO.class))
                .toList();
    }

    public FamiliaDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), FamiliaDTO.class);
    }

    public FamiliaDTO crear(FamiliaDTO dto) {
        Familia entidad = modelMapper.map(dto, Familia.class);
        return modelMapper.map(familiaRepository.save(entidad), FamiliaDTO.class);
    }

    public FamiliaDTO actualizar(Long id, FamiliaDTO dto) {
        Familia existente = buscarEntidad(id);
        Familia datos = modelMapper.map(dto, Familia.class);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return modelMapper.map(familiaRepository.save(existente), FamiliaDTO.class);
    }

    public void eliminar(Long id) {
        familiaRepository.delete(buscarEntidad(id));
    }

    /** Uso interno: el resto de servicios necesita la entidad, no el DTO. */
    public Familia buscarEntidad(Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con id: " + id));
    }
}
