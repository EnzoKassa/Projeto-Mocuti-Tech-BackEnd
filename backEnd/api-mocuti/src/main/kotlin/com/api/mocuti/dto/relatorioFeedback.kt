package com.api.mocuti.dto

import jakarta.persistence.Lob

/* isso é um DTO
*/

data class relatorioFeedback(
    val id: Int,
    val nota: Boolean,
    @Lob
    val comentario: String
)
