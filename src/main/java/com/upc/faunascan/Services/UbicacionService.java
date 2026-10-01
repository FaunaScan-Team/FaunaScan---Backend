package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.Repositories.UbicacionRepository;
import com.upc.faunascan.dto.UbicacionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final ModelMapper modelMapper;

    public List<UbicacionDTO> listar() {
        return ubicacionRepository.findAll().stream()
                .map(entidad -> modelMapper.map(entidad, UbicacionDTO.class))
                .toList();
    }

    public UbicacionDTO obtenerPorId(Long id) {
        return modelMapper.map(buscarEntidad(id), UbicacionDTO.class);
    }

    public UbicacionDTO crear(UbicacionDTO dto) {
        Ubicacion entidad = modelMapper.map(dto, Ubicacion.class);
        return modelMapper.map(ubicacionRepository.save(entidad), UbicacionDTO.class);
    }

    public UbicacionDTO actualizar(Long id, UbicacionDTO dto) {
        Ubicacion existente = buscarEntidad(id);
        Ubicacion datos = modelMapper.map(dto, Ubicacion.class);
        existente.setLatitud(datos.getLatitud());
        existente.setLongitud(datos.getLongitud());
        existente.setDireccion(datos.getDireccion());
        return modelMapper.map(ubicacionRepository.save(existente), UbicacionDTO.class);
    }

    public void eliminar(Long id) {
        ubicacionRepository.delete(buscarEntidad(id));
    }

    /** Uso interno: el resto de servicios necesita la entidad, no el DTO. */
    public Ubicacion buscarEntidad(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + id));
    }
}
