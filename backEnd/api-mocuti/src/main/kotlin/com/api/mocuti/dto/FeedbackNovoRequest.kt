package com.api.mocuti.dto

import jakarta.validation.constraints.NotNull

data class FeedbackNovoRequest(
    val comentario: String? = null,

    @field:NotNull
    val idEvento: Int,

    @field:NotNull
    val idUsuario: Int
)