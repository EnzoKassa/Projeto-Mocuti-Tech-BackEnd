package com.api.mocuti.entity

import jakarta.persistence.*

@Entity
data class Participacao (
    @EmbeddedId   // Indica que `id` é uma chave composta, baseada em uma classe `@Embeddable`
    val id: ParticipacaoId,

    val isInscrito: Boolean? = null,

    val isPresente: Boolean? = null,

    @ManyToOne
    @JoinColumn(name = "inscricao_participacao")
    val statusInscricao: StatusInscricao? = null,

    @MapsId("usuarioId")
    @ManyToOne
    @JoinColumn(name = "usuario_participacao")
    val usuario: Usuario? = null,

    @MapsId("eventoId")
    @ManyToOne
    @JoinColumn(name = "evento_participacao")
    val evento: Evento? = null
)
