package mocuti01.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDate

@Entity
data class Usuario(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var idUsuario: Int? = null,

    @field:NotBlank @field:Size(max = 45) var nomeCompleto: String,

    @field:NotBlank @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") var cpf: String,

    @field:Size(max = 20) @Column(length = 20) var telefone: String? = null,

    @field:NotBlank @field:Email @field:Size(max = 45) var email: String,

    @field:Past var dataNascimento: LocalDate?,

    @field:Size(max = 45) var genero: String? = null,

    @field:NotBlank @field:Size(max = 14) var senha: String,

    @JsonIgnore var isAutenticado: Boolean = false,

    @JsonIgnore var isAtivo: Boolean = true,

    @JsonIgnore var dtDesativacao: LocalDate? = null,

    @Column(name = "cargo_int", nullable = false) var cargoInt: Int? = null,

    @field:PositiveOrZero @Column(name = "fk_endereco_usuario", nullable = true)
    var fkEnderecoUsuario: Int? = null,

    @field:PositiveOrZero @Column(name = "fk_canal_comunicacao_usuario", nullable = true)
    var fkCanalComunicacaoUsuario: Int? = null,

    ) {
    constructor() : this(null, "", "", null, "", null, null, "", false, true, null, null) {
        this.dtDesativacao = null
    }
}