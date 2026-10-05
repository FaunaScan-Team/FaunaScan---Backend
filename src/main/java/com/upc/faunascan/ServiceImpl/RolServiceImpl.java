package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.RolService;
import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Repositories.RolRepository;
import com.upc.faunascan.dto.RolDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RolDTO> listar() {
        return rolRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, RolDTO.class))
                .toList();
    }

    @Override
    public RolDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), RolDTO.class);
    }

    @Override
    public RolDTO crear(RolDTO dto) {
        Rol entidad = modelMapper.map(dto, Rol.class);
        return modelMapper.map(rolRepository.save(entidad), RolDTO.class);
    }

    @Override
    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol existente = buscarEntidad(id);
        Rol datos = modelMapper.map(dto, Rol.class);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return modelMapper.map(rolRepository.save(existente), RolDTO.class);
    }

    @Override
    public void eliminar(Long id) {
        rolRepository.delete(buscarEntidad(id));
    }

    @Override
    public Rol buscarEntidad(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado con id: " + id));
    }
}
