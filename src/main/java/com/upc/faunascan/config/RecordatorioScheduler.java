package com.upc.faunascan.config;

import com.upc.faunascan.Services.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// HU-59: tarea diaria que recuerda participar a los voluntarios sin actividad en 7 dias
@Component
@RequiredArgsConstructor
public class RecordatorioScheduler {
    private final NotificacionService notificacionService;

    @Scheduled(cron = "${recordatorios.cron:0 0 9 * * *}", zone = "America/Lima")
    public void enviarRecordatoriosDeParticipacion() {
        notificacionService.enviarRecordatorios();
    }
}
