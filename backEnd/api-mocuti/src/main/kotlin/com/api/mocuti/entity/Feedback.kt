package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class Feedback(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    var id: Int?,


    @Column(nullable = false)
    @field:NotBlank
    val comentario: String?,

    @ManyToOne
    @JoinColumn(name = "fk_nota_feedback")
    var nota: NotaFeedback? = null,

    @ManyToOne
    @JoinColumn(name = "fk_evento_feedback")
    var evento: Evento? = null,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_feedback")
    var usuario: Usuario? = null,

    ) {
    // O JPA exige que exista um construtor vazio nas Entidades
    constructor() : this(null, null, null)
    /*
    Aqui dizemos que sempre que o construtor vazio for invocado,
    será criado uma Musica com id, nome e interprete nulos
     */
}
