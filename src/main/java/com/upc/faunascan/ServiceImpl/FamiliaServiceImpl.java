package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.FamiliaService;
import com.upc.faunascan.Entities.Familia;
import com.upc.faunascan.Repositories.FamiliaRepository;
import com.upc.faunascan.dto.FamiliaDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamiliaServiceImpl implements FamiliaService {
    private final FamiliaRepository familiaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FamiliaDTO> listar() {
        return familiaRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, FamiliaDTO.class))
                .toList();
    }

    @Override
    public FamiliaDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), FamiliaDTO.class);
    }

    @Override
    public FamiliaDTO crear(FamiliaDTO dto) {
        Familia entidad = modelMapper.map(dto, Familia.class);
        return modelMapper.map(familiaRepository.save(entidad), FamiliaDTO.class);
    }

    @Override
    public FamiliaDTO actualizar(Long id, FamiliaDTO dto) {
        Familia existente = buscarEntidad(id);
        Familia datos = modelMapper.map(dto, Familia.class);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return modelMapper.map(familiaRepository.save(existente), FamiliaDTO.class);
    }

    @Override
    public void eliminar(Long id) {
        familiaRepository.delete(buscarEntidad(id));
    }

    @Override
    public Familia buscarEntidad(Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Familia no encontrada con id: " + id));
    }
}
