package com.upc.faunascan.security.controllers;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.Services.UsuarioService;
import com.upc.faunascan.dto.ContrasenaTemporalDTO;
import com.upc.faunascan.dto.LoginDTO;
import com.upc.faunascan.dto.RecuperarContrasenaDTO;
import com.upc.faunascan.dto.UsuarioDTO;
import com.upc.faunascan.dto.UsuarioRegistroDTO;
import com.upc.faunascan.security.dtos.AuthResponseDTO;
import com.upc.faunascan.security.services.CustomUserDetailsService;
import com.upc.faunascan.security.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    // HU-01: registro de usuario
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO registrar(@Valid @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        return usuarioService.registrar(usuarioRegistroDTO);
    }

    // HU-02/HU-33: inicio de sesion
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getContrasena())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getCorreo());
        Usuario usuario = usuarioRepository.findByCorreo(loginDTO.getCorreo()).get();
        final String token = jwtUtil.generateToken(userDetails, usuario.getIdUsuario());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setJwt(token);
        authResponseDTO.setIdUsuario(usuario.getIdUsuario());
        authResponseDTO.setRol(usuario.getRol().getNombre());
        return ResponseEntity.ok().headers(responseHeaders).body(authResponseDTO);
    }

    // HU-03: recuperar la contrasena
    @PostMapping("/recuperar-password")
    public ContrasenaTemporalDTO recuperarContrasena(@Valid @RequestBody RecuperarContrasenaDTO recuperarContrasenaDTO) {
        String temporal = usuarioService.recuperarContrasena(recuperarContrasenaDTO.getCorreo());
        return new ContrasenaTemporalDTO(temporal);
    }
}
