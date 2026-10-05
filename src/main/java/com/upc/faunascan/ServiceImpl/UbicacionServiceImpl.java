package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.UbicacionService;
import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.Repositories.UbicacionRepository;
import com.upc.faunascan.dto.UbicacionDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {
    private final UbicacionRepository ubicacionRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UbicacionDTO> listar() {
        return ubicacionRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, UbicacionDTO.class))
                .toList();
    }

    @Override
    public UbicacionDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), UbicacionDTO.class);
    }

    @Override
    public UbicacionDTO crear(UbicacionDTO dto) {
        Ubicacion entidad = modelMapper.map(dto, Ubicacion.class);
        return modelMapper.map(ubicacionRepository.save(entidad), UbicacionDTO.class);
    }

    @Override
    public UbicacionDTO actualizar(Long id, UbicacionDTO dto) {
        Ubicacion existente = buscarEntidad(id);
        Ubicacion datos = modelMapper.map(dto, Ubicacion.class);
        existente.setLatitud(datos.getLatitud());
        existente.setLongitud(datos.getLongitud());
        existente.setDireccion(datos.getDireccion());
        return modelMapper.map(ubicacionRepository.save(existente), UbicacionDTO.class);
    }

    @Override
    public void eliminar(Long id) {
        ubicacionRepository.delete(buscarEntidad(id));
    }

    @Override
    public Ubicacion buscarEntidad(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ubicacion no encontrada con id: " + id));
    }
}
