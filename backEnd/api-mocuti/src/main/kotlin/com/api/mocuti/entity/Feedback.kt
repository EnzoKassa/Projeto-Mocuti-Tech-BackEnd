package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Feedback (
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @Lob
    @Column(nullable = false)
    val comentario: String?,

    @ManyToOne
    @JoinColumn(name = "feedback_nota")
    var nota: NotaFeedback? = null,

    @ManyToOne
    @JoinColumn(name = "feedback_evento")
    var evento: Evento? = null,

    @ManyToOne
    @JoinColumn(name = "feedback_usuario")
    var usuario: Usuario? = null,

    ) {
    // O JPA exige que exista um construtor vazio nas Entidades
    constructor() : this(null, null, null)
    /*
    Aqui dizemos que sempre que o construtor vazio for invocado,
    será criado uma Musica com id, nome e interprete nulos
     */
}
