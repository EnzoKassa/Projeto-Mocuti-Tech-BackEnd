package com.api.mocuti.dto

import com.api.mocuti.entity.StatusEvento
import java.time.LocalDate

data class EventoFiltroRequest(
    val nome: String? = null,
    val dataInicio: LocalDate? = null,
    val dataFim: LocalDate? = null,
    val categoriaId: Int? = null,
    val statusEventoId: Int? = null,
)