package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class Evento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idEvento: Int,

    @field:NotBlank @field:Size(min = 2, max = 45)
    var nomeEvento: String,

    @field:Size(min = 2, max = 255)
    var descricao: String,

    var dia: LocalDate,

    var horaInicio: LocalTime,

    var horaFim: LocalTime,

    @Column(columnDefinition = "tinyint")
    var isAberto: Boolean,

    @field:PositiveOrZero
    var qtdVaga: Int? = null,

    @field:PositiveOrZero
    var qtdInteressado: Int? = null,

    @JsonIgnore
    @Column(length = 100 * 1024 * 1024)
    var foto: ByteArray? = null,

    @ManyToOne
    @JoinColumn(name = "fk_endereco_evento")
    var endereco: Endereco? = null,

    @ManyToOne
    @JoinColumn(name = "fk_status_evento")
    var statusEvento: StatusEvento? = null,

    @ManyToOne
    @JoinColumn(name = "fk_publico_alvo_evento")
    var publicoAlvoEvento: PublicoAlvo? = null,

    @ManyToOne
    @JoinColumn(name = "fk_categoria_evento")
    var categoria: Categoria? = null
) {
    constructor() : this(
        0, "", "", LocalDate.now(), LocalTime.now(), LocalTime.now(),
        false, null, null, null, null, null,
        null, null
    )
}
