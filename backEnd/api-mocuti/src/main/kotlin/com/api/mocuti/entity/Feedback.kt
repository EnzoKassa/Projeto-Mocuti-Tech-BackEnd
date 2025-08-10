package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Feedback(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idFeedback: Int,

    var comentario: String? = null,

    @JsonIgnore
    var data_feedback: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "fk_nota_feedback")
    var nota: NotaFeedback? = null,

    @ManyToOne
    @JoinColumn(name = "fk_evento_feedback")
    var evento: Evento,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_feedback")
    var usuario: Usuario
)
