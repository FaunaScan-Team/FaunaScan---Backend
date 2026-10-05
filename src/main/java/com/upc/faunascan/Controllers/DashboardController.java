package com.upc.faunascan.Controllers;

import com.upc.faunascan.Services.DashboardService;
import com.upc.faunascan.dto.DashboardInvestigadorDTO;
import com.upc.faunascan.dto.DashboardVoluntarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    // HU-52: panel principal del investigador
    @GetMapping("/investigador")
    @PreAuthorize("hasRole('INVESTIGADOR')")
    public DashboardInvestigadorDTO investigador(Authentication authentication) {
        return dashboardService.panelInvestigador(authentication.getName());
    }

    // HU-53: panel principal del voluntario
    @GetMapping("/voluntario")
    @PreAuthorize("hasRole('VOLUNTARIO')")
    public DashboardVoluntarioDTO voluntario(Authentication authentication) {
        return dashboardService.panelVoluntario(authentication.getName());
    }
}
