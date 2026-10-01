package com.upc.faunascan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private Long idRol;
    private String nombreRol;
    private Long idPerfil;
    private String nombre;
    private String apellido;
    private String correo;
    private LocalDateTime fechaRegistro;
    private Boolean estado;
    private String urlCredencial;
    private String preferenciasNotificaciones;
}
