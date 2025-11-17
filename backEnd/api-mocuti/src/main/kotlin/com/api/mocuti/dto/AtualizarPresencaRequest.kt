package com.api.mocuti.dto

data class AtualizarPresencaRequest(
    val usuarioId: Int,
    val eventoId: Int,
    val statusInscricaoId: Int
)
