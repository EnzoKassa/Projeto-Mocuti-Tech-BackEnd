package com.api.mocuti.dto

import java.time.LocalDate

data class ParticipacaoResponse(
    val idEvento: Int,
    val nomeEvento: String,
    val dia: LocalDate,
    val status: Int // <-- Aqui pega o número real do banco

)
