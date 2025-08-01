package com.api.mocuti.dto

import java.time.LocalDate
import java.time.LocalTime

data class EventoCadastroRequest(
    val nomeEvento: String,
    val descricao: String,
    val dia: LocalDate,
    val horaInicio: LocalTime,
    val horaFim: LocalTime,
    val isAberto: Boolean,
    val qtdVaga: Int,
    val qtdInteressado: Int,
    val enderecoId: Int,
    val statusEventoId: Int,
    val publicoAlvoEventoId: Int,
    val categoriaId: Int
)