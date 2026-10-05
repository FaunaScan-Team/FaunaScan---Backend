package com.upc.faunascan.Services;

import com.upc.faunascan.dto.DashboardInvestigadorDTO;
import com.upc.faunascan.dto.DashboardVoluntarioDTO;

public interface DashboardService {
    DashboardInvestigadorDTO panelInvestigador(String correo);
    DashboardVoluntarioDTO panelVoluntario(String correo);
}
