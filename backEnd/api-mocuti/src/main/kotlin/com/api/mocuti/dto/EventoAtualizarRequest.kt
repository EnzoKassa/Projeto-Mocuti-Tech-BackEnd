package com.api.mocuti.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalTime
import jakarta.validation.constraints.*

data class EventoAtualizarRequest(
    @field:NotBlank
    val nomeEvento: String,

    @field:NotBlank
    @field:Size(max = 255)
    val descricao: String,

    @field:NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    val dia: LocalDate,

    @field:NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    val horaInicio: LocalTime,

    @field:NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    val horaFim: LocalTime,

    @field:NotNull
    val isAberto: Boolean,

    @field:PositiveOrZero
    val qtdVaga: Int,


    @field:PositiveOrZero
    val qtdInteressado: Int,

    @field:NotNull
    val publicoAlvo: String,

    @field:NotNull
    val enderecoId: Int,

    @field:NotNull
    val statusEventoId: Int,

    @field:NotNull
    val categoriaId: Int
)