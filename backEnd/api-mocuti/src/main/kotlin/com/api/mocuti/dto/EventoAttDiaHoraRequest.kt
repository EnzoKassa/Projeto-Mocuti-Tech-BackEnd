package com.api.mocuti.dto

import java.time.LocalDate
import java.time.LocalTime

data class EventoAttDiaHoraRequest(
    val dia: LocalDate,
    val horaInicio: LocalTime,
    val horaFim: LocalTime
)