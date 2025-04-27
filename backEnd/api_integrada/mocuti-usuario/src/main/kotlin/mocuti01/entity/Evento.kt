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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var idEvento: Int,
    @field:NotBlank @field:Size(min = 2, max = 45) var nomeEvento: String,
    @field:Size(min = 2, max = 255) var descricao: String,
    var dia: LocalDate,
    var horaInicio: Time,
    var horaFim: Time,
    var isAberto: Boolean,
    @field:PositiveOrZero var qtdVaga: Int? = null,
    @field:PositiveOrZero var qtdInteressado: Int? = null,
    @Column(length = 100 * 1024 * 1024)
    @JsonIgnore
    var foto: ByteArray? = null,
    var fkEnderecoEvento: Int,
    var fkStatusEvento: Int,
    var fkPublicoAlvoEvento: Int,
    var fkCategoriaEvento: Int
) {
    constructor() : this(
        0, "", "", LocalDate.now(), Time(0), Time(0),
        false, null, null, null, 0, 0,
        0, 0
    )
}
