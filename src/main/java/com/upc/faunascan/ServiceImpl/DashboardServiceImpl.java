package com.upc.faunascan.ServiceImpl;

import com.upc.faunascan.Entities.Usuario;
import com.upc.faunascan.Repositories.AvistamientoRepository;
import com.upc.faunascan.Repositories.ReporteRepository;
import com.upc.faunascan.Repositories.UsuarioRepository;
import com.upc.faunascan.Services.DashboardService;
import com.upc.faunascan.Services.EspecieService;
import com.upc.faunascan.dto.DashboardInvestigadorDTO;
import com.upc.faunascan.dto.DashboardVoluntarioDTO;
import com.upc.faunascan.exceptions.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UsuarioRepository usuarioRepository;
    private final AvistamientoRepository avistamientoRepository;
    private final ReporteRepository reporteRepository;
    private final EspecieService especieService;

    // HU-52: datos del investigador + pendientes de validacion para sus accesos directos
    @Override
    @Transactional(readOnly = true)
    public DashboardInvestigadorDTO panelInvestigador(String correo) {
        Usuario usuario = buscarUsuario(correo);
        DashboardInvestigadorDTO dto = new DashboardInvestigadorDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setRol(usuario.getRol().getNombre());
        dto.setPendientesValidacion(avistamientoRepository.countByEstadoValidacion("pendiente"));
        dto.setValidadosPorMi(avistamientoRepository.countByInvestigadorValidador_IdUsuario(usuario.getIdUsuario()));
        dto.setReportesGenerados(reporteRepository.countByUsuario_IdUsuario(usuario.getIdUsuario()));
        return dto;
    }

    // HU-53: progreso del voluntario + guias de identificacion
    @Override
    @Transactional(readOnly = true)
    public DashboardVoluntarioDTO panelVoluntario(String correo) {
        Usuario usuario = buscarUsuario(correo);
        DashboardVoluntarioDTO dto = new DashboardVoluntarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setRol(usuario.getRol().getNombre());
        dto.setProgreso(avistamientoRepository.calcularProgreso(usuario.getIdUsuario()));
        dto.setGuias(especieService.listarGuiasDestacadas());
        return dto;
    }

    private Usuario buscarUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con correo: " + correo));
    }
}
