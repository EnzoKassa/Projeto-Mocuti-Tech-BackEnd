package com.api.mocuti.dto

data class FeedbackNovoRequest(
    val comentario: String? = null,
    val idEvento: Int,
    val idUsuario: Int,
    val idNota: Int? = null
)