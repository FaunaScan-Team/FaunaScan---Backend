package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Rol;
import com.upc.faunascan.Repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    public Rol crear(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol actualizar(Long id, Rol datos) {
        Rol rol = obtenerPorId(id);
        rol.setNombre(datos.getNombre());
        rol.setDescripcion(datos.getDescripcion());
        return rolRepository.save(rol);
    }

    public void eliminar(Long id) {
        rolRepository.delete(obtenerPorId(id));
    }
}
