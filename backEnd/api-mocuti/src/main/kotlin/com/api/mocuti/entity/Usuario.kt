package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDate

@Entity
data class Usuario(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idUsuario: Int,

    @field:NotBlank @field:Size(max = 45)
    var nomeCompleto: String,

    @field:NotBlank @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")
    @Column(columnDefinition = "char(14)")
    var cpf: String,

    @field:Size(max = 20) @Column(columnDefinition = "char(11)")
    var telefone: String? = null,

    @field:NotBlank @field:Email @field:Size(max = 45)
    var email: String,

    @field:Past var
    dt_nasc: LocalDate?,

    @field:Size(max = 45)
    var genero: String? = null,

    @field:NotBlank @field:Size(max = 14)
    var senha: String,

    @Column(columnDefinition = "tinyint")
    @JsonIgnore
    var isAutenticado: Boolean = false,

    @Column(columnDefinition = "tinyint")
    @JsonIgnore
    var isAtivo: Boolean = true,

    @JsonIgnore
    var dtDesativacao: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "fk_cargo_usuario")
    var cargo: Cargo? = null,

    @ManyToOne
    @JoinColumn(name = "fk_endereco_usuario")
    var endereco: Endereco? = null,

    @ManyToOne
    @JoinColumn(name = "fk_canal_comunicacao_usuario")
    var canalComunicacao: CanalComunicacao? = null,

    ) {
    constructor() : this(
        0, "", "", null, "", null,
        null, "", false, true, null, null
    ) {
        this.dtDesativacao = null
    }
}