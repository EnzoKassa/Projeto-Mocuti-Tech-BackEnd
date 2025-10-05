package com.api.mocuti.dto

import com.api.mocuti.entity.Categoria
import com.api.mocuti.entity.StatusEvento
import java.time.LocalDate
import java.time.LocalTime

data class EventoDTO(
    val idEvento: Int?,
    val nomeEvento: String?,
    val nome: String,
    val descricao: String?,
    val dia: LocalDate,
    val horaInicio: LocalTime?,
    val horaFim: LocalTime?,
    val qtdVaga: Int?,
    val qtdInteressado: Int?,
    val publicoAlvo: String?,
    val statusEvento: StatusEvento?,
    val categoria: Categoria?
)