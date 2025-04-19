package com.evento.evento.mocuti.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import org.springframework.cglib.core.Local
import java.sql.Time
import java.time.LocalDate
import java.util.Date

@Entity
data class Evento(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id_evento: Int,
    @field:NotBlank @field:Size(min = 2, max = 45) var nome_evento: String,
    @field:Size(min = 2, max = 255) var descricao: String,
    var dia: LocalDate,
    var hora_inicio: Time,
    var hora_fim: Time,
    var is_aberto: Boolean,
    @field:PositiveOrZero var qtd_vaga: Int? = null,
    @field:PositiveOrZero var qtd_interessado: Int? = null,
    @Column(length = 100 * 1024 * 1024)
    @JsonIgnore
    var foto: ByteArray? = null,
    var fk_endereco_evento: Int,
    var fk_status_evento: Int,
    var fk_publico_alvo_evento: Int,
    var fk_categoria_evento: Int
) {
    constructor() : this(
        0, "", "", LocalDate.now(), Time(0), Time(0),
        false, null, null, null, 0, 0,
        0, 0
    )
}
