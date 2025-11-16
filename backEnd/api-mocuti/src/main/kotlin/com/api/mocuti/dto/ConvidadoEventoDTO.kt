package com.api.mocuti.dto

data class ConvidadoEventoDTO(
    val idEvento: Int,
    val idUsuario: Int,
    val nomeConvidado: String,
    val statusConvite: String
)