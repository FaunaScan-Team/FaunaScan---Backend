package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Repositories.RolRepository;
import com.upc.faunascan.dto.RolDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    public List<RolDTO> listar() {
        return rolRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, RolDTO.class))
                .toList();
    }

    public RolDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), RolDTO.class);
    }

    public RolDTO crear(RolDTO dto) {
        Rol entidad = modelMapper.map(dto, Rol.class);
        return modelMapper.map(rolRepository.save(entidad), RolDTO.class);
    }

    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol existente = buscarEntidad(id);
        Rol datos = modelMapper.map(dto, Rol.class);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return modelMapper.map(rolRepository.save(existente), RolDTO.class);
    }

    public void eliminar(Long id) {
        rolRepository.delete(buscarEntidad(id));
    }

    public Rol buscarEntidad(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }
}
