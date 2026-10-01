package com.upc.faunascan.security.controllers;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.dto.LoginDTO;
import com.upc.faunascan.security.dtos.AuthResponseDTO;
import com.upc.faunascan.security.services.CustomUserDetailsService;
import com.upc.faunascan.security.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
    }

    // US02: iniciar sesion
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
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
}
