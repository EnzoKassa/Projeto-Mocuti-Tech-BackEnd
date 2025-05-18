package mocuti01.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.sql.Time
import java.time.LocalDate

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

    var horaInicio: Time,

    var horaFim: Time,

    var isAberto: Boolean,

    @field:PositiveOrZero
    var qtdVaga: Int? = null,

    @field:PositiveOrZero
    var qtdInteressado: Int? = null,

    @JsonIgnore
    @Column(length = 100 * 1024 * 1024)
    var foto: ByteArray? = null,

    @ManyToOne
    @JoinColumn(name = "endereco_evento")
    var endereco: Endereco? = null,

    var statusEvento: Int? = null,

    var publicoAlvoEvento: Int? = null,

    @ManyToOne
    @JoinColumn(name = "categoria_evento")
    var categoria: Categoria? = null
) {
    constructor() : this(
        0, "", "", LocalDate.now(), Time(0), Time(0),
        false, null, null, null, null, 0,
        0, null
    )
}
