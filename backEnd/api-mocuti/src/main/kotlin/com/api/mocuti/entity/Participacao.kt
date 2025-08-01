package com.api.mocuti.entity

import jakarta.persistence.*

@Entity
data class Participacao(
    @EmbeddedId  // Indica que `id` é uma chave composta, baseada em uma classe `@Embeddable`
    val id: ParticipacaoId,

    @Column(columnDefinition = "tinyint")
    val isInscrito: Boolean,

    @Column(columnDefinition = "tinyint")
    val isPresente: Boolean,

    @ManyToOne
    @JoinColumn(name = "fk_inscricao_participacao")
    val statusInscricao: StatusInscricao,

    @MapsId("usuarioId")
    @ManyToOne
    @JoinColumn(name = "fk_usuario_participacao")
    val usuario: Usuario,

    @MapsId("eventoId")
    @ManyToOne
    @JoinColumn(name = "fk_evento_participacao")
    val evento: Evento
)
