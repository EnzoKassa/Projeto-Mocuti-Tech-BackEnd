package com.api.mocuti.entity

import jakarta.persistence.*

@Entity
@Table(name = "participacao", schema = "mocuti")
data class Participacao(
    @EmbeddedId
    val id: ParticipacaoId,

//    @Column(name = "is_inscrito")
    val isInscrito: Boolean? = null,

//    @Column(name = "is_presente")
    val isPresente: Boolean? = null,

//    @ManyToOne
//    @JoinColumn(name = "fk_inscricao_participacao", referencedColumnName = "id_inscricao")
    val statusInscricao: StatusInscricao? = null,

//    @ManyToOne
//    @MapsId("usuarioId") // liga ao campo do ID composto
//    @JoinColumn(name = "fk_usuario_participacao", referencedColumnName = "id_usuario")
    val usuario: Usuario? = null,

//    @ManyToOne
//    @MapsId("eventoId")
//    @JoinColumn(name = "fk_evento_participacao", referencedColumnName = "id_evento")
    val evento: Evento? = null
)
