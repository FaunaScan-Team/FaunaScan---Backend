package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Services.ImagenAvistamientoService;
import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import com.upc.faunascan.dto.ImagenAvistamientoDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

    // HU-10: adjuntar foto(s) a un avistamiento
@Service
@RequiredArgsConstructor
public class ImagenAvistamientoServiceImpl implements ImagenAvistamientoService {
    private final ImagenAvistamientoRepository imagenAvistamientoRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ModelMapper modelMapper;

    @Override
    public ImagenAvistamientoDTO agregar(ImagenAvistamientoDTO dto) {
        ImagenAvistamiento imagen = modelMapper.map(dto, ImagenAvistamiento.class);
        imagen.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        return aDTO(imagenAvistamientoRepository.save(imagen));
    }

    @Override
    public List<ImagenAvistamientoDTO> listarPorAvistamiento(Long idAvistamiento) {
        return imagenAvistamientoRepository.findByAvistamiento_IdAvistamiento(idAvistamiento)
                .stream().map(this::aDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        imagenAvistamientoRepository.deleteById(id);
    }

    private ImagenAvistamientoDTO aDTO(ImagenAvistamiento imagen) {
        ImagenAvistamientoDTO dto = modelMapper.map(imagen, ImagenAvistamientoDTO.class);
        if (imagen.getAvistamiento() != null) {
            dto.setIdAvistamiento(imagen.getAvistamiento().getIdAvistamiento());
        }
        return dto;
    }
}
