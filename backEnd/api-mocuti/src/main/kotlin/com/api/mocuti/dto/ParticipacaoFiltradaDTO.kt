package com.api.mocuti.dto

import com.api.mocuti.entity.NotaFeedback
import com.api.mocuti.entity.ParticipacaoId

data class ParticipacaoFeedbackDTO(
    val id: ParticipacaoId,
    val isPresente: Boolean,
    val email: String,
    val nomeEvento: String,
    val nota: NotaFeedback? = null,
    val comentario: String? = null
)