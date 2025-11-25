package com.api.mocuti.config

import com.api.mocuti.service.EventoService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks(
    private val eventoService: EventoService
) {
    @Scheduled(fixedRate = 60000) // Roda a cada 1 minuto (60000 milissegundos)
    fun atualizarStatusEventosAgendado() {
        println("Iniciando verificação de status de eventos...")
        eventoService.atualizarStatusEventos()
    }
}