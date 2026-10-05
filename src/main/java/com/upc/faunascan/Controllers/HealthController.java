package com.upc.faunascan.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

// HU-40: estado del backend para el monitoreo de disponibilidad (no requiere token)
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    private final DataSource dataSource;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        boolean baseDatosOk;
        try (Connection conexion = dataSource.getConnection()) {
            baseDatosOk = conexion.isValid(2);
        } catch (Exception e) {
            baseDatosOk = false;
        }
        Map<String, Object> estado = new LinkedHashMap<>();
        estado.put("estado", baseDatosOk ? "UP" : "DOWN");
        estado.put("baseDatos", baseDatosOk ? "UP" : "DOWN");
        estado.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(baseDatosOk ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE).body(estado);
    }
}
