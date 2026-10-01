package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.ImagenAvistamiento;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.ImagenAvistamientoRepository;
import com.upc.faunascan.dto.ImagenAvistamientoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// US10: fotos adjuntas a un avistamiento
@Service
@RequiredArgsConstructor
public class ImagenAvistamientoService {

    private final ImagenAvistamientoRepository imagenAvistamientoRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ModelMapper modelMapper;

    public ImagenAvistamientoDTO agregar(ImagenAvistamientoDTO dto) {
        ImagenAvistamiento imagen = modelMapper.map(dto, ImagenAvistamiento.class);
        imagen.setAvistamiento(avistamientoRepository.findById(dto.getIdAvistamiento())
                .orElseThrow(() -> new RuntimeException(
                        "Avistamiento no encontrado con id: " + dto.getIdAvistamiento())));
        return aDTO(imagenAvistamientoRepository.save(imagen));
    }

    public List<ImagenAvistamientoDTO> listarPorAvistamiento(Long idAvistamiento) {
        return imagenAvistamientoRepository.findByAvistamiento_IdAvistamiento(idAvistamiento)
                .stream().map(this::aDTO).toList();
    }

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
