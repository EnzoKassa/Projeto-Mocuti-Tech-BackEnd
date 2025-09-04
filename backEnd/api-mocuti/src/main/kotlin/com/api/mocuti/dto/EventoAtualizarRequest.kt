package com.api.mocuti.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalTime
import jakarta.validation.constraints.*

data class EventoAtualizarRequest(
    val nomeEvento: String,
    val descricao: String,
    val dia: LocalDate,
    val horaInicio: LocalTime,
    val horaFim: LocalTime,
    val isAberto: Boolean,
    val qtdVaga: Int,
    val qtdInteressado: Int,
    val publicoAlvo: String,
    val enderecoId: Int,
    val statusEventoId: Int,
    val categoriaId: Int
)