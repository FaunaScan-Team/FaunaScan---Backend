package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Perfil;
import com.upc.faunascan.Repositories.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public Perfil obtenerPorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado con id: " + id));
    }

    // US04: crea el perfil vacio asociado a un usuario nuevo
    public Perfil crear(Perfil perfil) {
        perfil.setFechaActualizacion(LocalDateTime.now());
        return perfilRepository.save(perfil);
    }

    // US05: editar perfil (biografia, ubicacion, foto se maneja en Usuario.urlCredencial)
    public Perfil actualizar(Long id, Perfil datos) {
        Perfil perfil = obtenerPorId(id);
        perfil.setBiografia(datos.getBiografia());
        perfil.setUbicacion(datos.getUbicacion());
        perfil.setFechaActualizacion(LocalDateTime.now());
        return perfilRepository.save(perfil);
    }

    public void eliminar(Long id) {
        perfilRepository.delete(obtenerPorId(id));
    }
}
