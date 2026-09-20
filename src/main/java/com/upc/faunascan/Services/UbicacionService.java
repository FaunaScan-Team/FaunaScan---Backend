package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Ubicacion;
import com.upc.faunascan.Repositories.UbicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    public Ubicacion obtenerPorId(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + id));
    }

    public Ubicacion crear(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    public Ubicacion actualizar(Long id, Ubicacion datos) {
        Ubicacion ubicacion = obtenerPorId(id);
        ubicacion.setLatitud(datos.getLatitud());
        ubicacion.setLongitud(datos.getLongitud());
        ubicacion.setDireccion(datos.getDireccion());
        return ubicacionRepository.save(ubicacion);
    }

    public void eliminar(Long id) {
        ubicacionRepository.delete(obtenerPorId(id));
    }
}
