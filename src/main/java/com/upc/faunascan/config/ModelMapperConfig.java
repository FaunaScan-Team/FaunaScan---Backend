package com.upc.faunascan.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean de ModelMapper para convertir entidades a DTO y viceversa.
 *
 * Se usa la estrategia STRICT: solo se copian las propiedades con el mismo
 * nombre y tipo. Asi se evita que ModelMapper intente adivinar relaciones
 * (por ejemplo, Avistamiento tiene dos campos Usuario: "usuario" e
 * "investigadorValidador", y con la estrategia por defecto ambos compiten por
 * el campo idUsuario del DTO). Las relaciones las resuelve cada servicio
 * buscando la entidad por su id en el repositorio correspondiente.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
