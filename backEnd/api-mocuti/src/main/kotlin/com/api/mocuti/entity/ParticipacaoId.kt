package com.api.mocuti.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class ParticipacaoId(
//    @Column(name = "fk_usuario_participacao")
    val usuarioId: Int = 0,

//    @Column(name = "fk_evento_participacao")
    val eventoId: Int = 0
) : Serializable
