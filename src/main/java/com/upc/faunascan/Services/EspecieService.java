package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Repositories.CategoriaConservacionRepository;
import com.upc.faunascan.Repositories.EspecieRepository;
import com.upc.faunascan.Repositories.FamiliaRepository;
import com.upc.faunascan.dto.EspecieDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;
    private final FamiliaRepository familiaRepository;
    private final CategoriaConservacionRepository categoriaConservacionRepository;
    private final ModelMapper modelMapper;

    public List<EspecieDTO> listar() {
        return especieRepository.findAll().stream().map(this::aDTO).toList();
    }

    public EspecieDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // US46: buscador por nombre comun o cientifico
    public List<EspecieDTO> buscar(String texto) {
        return especieRepository.buscar(texto).stream().map(this::aDTO).toList();
    }

    public List<EspecieDTO> listarPorFamilia(Long idFamilia) {
        return especieRepository.findByFamilia_IdFamilia(idFamilia).stream().map(this::aDTO).toList();
    }

    public List<EspecieDTO> listarPorCategoria(Long idCategoria) {
        return especieRepository.findByCategoriaConservacion_IdCategoria(idCategoria).stream()
                .map(this::aDTO).toList();
    }

    public EspecieDTO crear(EspecieDTO dto) {
        Especie especie = modelMapper.map(dto, Especie.class);
        asignarRelaciones(especie, dto);
        return aDTO(especieRepository.save(especie));
    }

    public EspecieDTO actualizar(Long id, EspecieDTO dto) {
        Especie especie = buscarEntidad(id);
        especie.setNombreComun(dto.getNombreComun());
        especie.setNombreCientifico(dto.getNombreCientifico());
        especie.setImagenReferencia(dto.getImagenReferencia());
        especie.setPistasIdentificacion(dto.getPistasIdentificacion());
        asignarRelaciones(especie, dto);
        return aDTO(especieRepository.save(especie));
    }

    public void eliminar(Long id) {
        especieRepository.delete(buscarEntidad(id));
    }

    public Especie buscarEntidad(Long id) {
        return especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada con id: " + id));
    }

    /** Resuelve las relaciones que en el DTO viajan como id. */
    private void asignarRelaciones(Especie especie, EspecieDTO dto) {
        if (dto.getIdFamilia() != null) {
            especie.setFamilia(familiaRepository.findById(dto.getIdFamilia())
                    .orElseThrow(() -> new RuntimeException("Familia no encontrada con id: " + dto.getIdFamilia())));
        }
        if (dto.getIdCategoria() != null) {
            especie.setCategoriaConservacion(categoriaConservacionRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoria de conservacion no encontrada con id: " + dto.getIdCategoria())));
        }
    }

    private EspecieDTO aDTO(Especie especie) {
        EspecieDTO dto = modelMapper.map(especie, EspecieDTO.class);
        if (especie.getFamilia() != null) {
            dto.setIdFamilia(especie.getFamilia().getIdFamilia());
            dto.setNombreFamilia(especie.getFamilia().getNombre());
        }
        if (especie.getCategoriaConservacion() != null) {
            dto.setIdCategoria(especie.getCategoriaConservacion().getIdCategoria());
            dto.setNombreCategoria(especie.getCategoriaConservacion().getNombre());
        }
        return dto;
    }
}
