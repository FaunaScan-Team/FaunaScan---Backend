package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.CategoriaConservacion;
import com.upc.faunascan.Repositories.CategoriaConservacionRepository;
import com.upc.faunascan.dto.CategoriaConservacionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaConservacionService {
    private final CategoriaConservacionRepository categoriaConservacionRepository;
    private final ModelMapper modelMapper;

    public List<CategoriaConservacionDTO> listar() {
        return categoriaConservacionRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, CategoriaConservacionDTO.class))
                .toList();
    }

    public CategoriaConservacionDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), CategoriaConservacionDTO.class);
    }

    public CategoriaConservacionDTO crear(CategoriaConservacionDTO dto) {
        CategoriaConservacion entidad = modelMapper.map(dto, CategoriaConservacion.class);
        return modelMapper.map(categoriaConservacionRepository.save(entidad), CategoriaConservacionDTO.class);
    }

    public CategoriaConservacionDTO actualizar(Long id, CategoriaConservacionDTO dto) {
        CategoriaConservacion existente = buscarEntidad(id);
        CategoriaConservacion datos = modelMapper.map(dto, CategoriaConservacion.class);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return modelMapper.map(categoriaConservacionRepository.save(existente), CategoriaConservacionDTO.class);
    }

    public void eliminar(Long id) {
        categoriaConservacionRepository.delete(buscarEntidad(id));
    }

    public CategoriaConservacion buscarEntidad(Long id) {
        return categoriaConservacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria de conservacion no encontrada con id: " + id));
    }
}
