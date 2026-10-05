package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.EspecieService;
import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Repositories.CategoriaConservacionRepository;
import com.upc.faunascan.Repositories.EspecieRepository;
import com.upc.faunascan.Repositories.FamiliaRepository;
import com.upc.faunascan.dto.EspecieDTO;
import com.upc.faunascan.dto.GuiaIdentificacionDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecieServiceImpl implements EspecieService {
    private final EspecieRepository especieRepository;
    private final FamiliaRepository familiaRepository;
    private final CategoriaConservacionRepository categoriaConservacionRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EspecieDTO> listar() {
        return especieRepository.findAll().stream().map(this::aDTO).toList();
    }

    @Override
    public EspecieDTO obtenerPorId(Long id) {
        return aDTO(buscarEntidad(id));
    }

    // HU-07: buscar especies por nombre comun o cientifico
    @Override
    public List<EspecieDTO> buscar(String texto) {
        return especieRepository.buscar(texto).stream().map(this::aDTO).toList();
    }

    @Override
    public List<EspecieDTO> listarPorFamilia(Long idFamilia) {
        return especieRepository.findByFamilia_IdFamilia(idFamilia).stream().map(this::aDTO).toList();
    }

    @Override
    public List<EspecieDTO> listarPorCategoria(Long idCategoria) {
        return especieRepository.findByCategoriaConservacion_IdCategoria(idCategoria).stream()
                .map(this::aDTO).toList();
    }

    @Override
    public EspecieDTO crear(EspecieDTO dto) {
        Especie especie = modelMapper.map(dto, Especie.class);
        asignarRelaciones(especie, dto);
        return aDTO(especieRepository.save(especie));
    }

    @Override
    public EspecieDTO actualizar(Long id, EspecieDTO dto) {
        Especie especie = buscarEntidad(id);
        especie.setNombreComun(dto.getNombreComun());
        especie.setNombreCientifico(dto.getNombreCientifico());
        especie.setImagenReferencia(dto.getImagenReferencia());
        especie.setPistasIdentificacion(dto.getPistasIdentificacion());
        asignarRelaciones(especie, dto);
        return aDTO(especieRepository.save(especie));
    }

    @Override
    public void eliminar(Long id) {
        especieRepository.delete(buscarEntidad(id));
    }

    @Override
    public Especie buscarEntidad(Long id) {
        return especieRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie no encontrada con id: " + id));
    }

    // HU-30: datos de las especies alternativas sugeridas por la IA, en el mismo orden de los ids
    @Override
    @Transactional(readOnly = true)
    public List<EspecieDTO> obtenerDetallesMultiples(List<Long> ids) {
        Map<Long, Especie> encontradas = especieRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Especie::getIdEspecie, Function.identity()));
        return ids.stream().distinct().filter(encontradas::containsKey)
                .map(id -> aDTO(encontradas.get(id))).toList();
    }

    // HU-58: guia visual de identificacion (especie + familia + categoria de conservacion)
    @Override
    @Transactional(readOnly = true)
    public GuiaIdentificacionDTO obtenerGuia(Long id) {
        return aGuia(buscarEntidad(id));
    }

    // HU-53: guias que se muestran en el panel del voluntario
    @Override
    @Transactional(readOnly = true)
    public List<GuiaIdentificacionDTO> listarGuiasDestacadas() {
        return especieRepository.findTop5ByPistasIdentificacionIsNotNullOrderByNombreComunAsc()
                .stream().map(this::aGuia).toList();
    }

    private GuiaIdentificacionDTO aGuia(Especie especie) {
        GuiaIdentificacionDTO guia = new GuiaIdentificacionDTO();
        guia.setIdEspecie(especie.getIdEspecie());
        guia.setNombreComun(especie.getNombreComun());
        guia.setNombreCientifico(especie.getNombreCientifico());
        guia.setImagenReferencia(especie.getImagenReferencia());
        guia.setPistasIdentificacion(especie.getPistasIdentificacion());
        if (especie.getFamilia() != null) {
            guia.setFamilia(especie.getFamilia().getNombre());
        }
        if (especie.getCategoriaConservacion() != null) {
            guia.setEstadoConservacion(especie.getCategoriaConservacion().getNombre());
        }
        return guia;
    }

    private void asignarRelaciones(Especie especie, EspecieDTO dto) {
        if (dto.getIdFamilia() != null) {
            especie.setFamilia(familiaRepository.findById(dto.getIdFamilia())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Familia no encontrada con id: " + dto.getIdFamilia())));
        }
        if (dto.getIdCategoria() != null) {
            especie.setCategoriaConservacion(categoriaConservacionRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
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
