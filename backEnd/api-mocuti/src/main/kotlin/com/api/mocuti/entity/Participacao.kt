package com.api.mocuti.entity

import jakarta.persistence.*

@Entity
data class Participacao(
    @EmbeddedId
    val id: ParticipacaoId,

    @Column(columnDefinition = "tinyint")
    var isInscrito: Boolean,

    @Column(columnDefinition = "tinyint")
    var isPresente: Boolean,

    @Column(columnDefinition = "varchar(45)")
    var statusConvite: String = "PENDENTE", // Valores: PENDENTE, ACEITO, RECUSADO

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