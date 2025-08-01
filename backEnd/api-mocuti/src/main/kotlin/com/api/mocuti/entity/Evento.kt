package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class Evento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idEvento: Int,

    @field:NotBlank
    @field:Size(min = 2, max = 45)
    var nomeEvento: String,

    @field:Size(min = 2, max = 255)
    @field:NotBlank
    var descricao: String,

    @field:NotNull
    @field:FutureOrPresent
    var dia: LocalDate,

    @field:NotNull
    var horaInicio: LocalTime,

    @field:NotNull
    var horaFim: LocalTime,

    @field:NotNull
    @Column(columnDefinition = "tinyint")
    var isAberto: Boolean,

    @field:PositiveOrZero
    var qtdVaga: Int,

    @field:PositiveOrZero
    var qtdInteressado: Int,

    @JsonIgnore
    @Column(length = 100 * 1024 * 1024)
    var foto: ByteArray? = null,

    @ManyToOne
    @JoinColumn(name = "fk_endereco_evento")
    var endereco: Endereco,

    @ManyToOne
    @JoinColumn(name = "fk_status_evento")
    var statusEvento: StatusEvento,

    @ManyToOne
    @JoinColumn(name = "fk_publico_alvo_evento")
    var publicoAlvoEvento: PublicoAlvo,

    @ManyToOne
    @JoinColumn(name = "fk_categoria_evento")
    var categoria: Categoria
)