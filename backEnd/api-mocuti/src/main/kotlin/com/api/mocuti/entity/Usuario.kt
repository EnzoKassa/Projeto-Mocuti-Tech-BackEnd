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

    @field:NotBlank @field:Size(max = 125)
    var nomeCompleto: String,

    @field:NotNull
    @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}") // CPF format: XXX.XXX.XXX-XX
    @Column(columnDefinition = "char(14)")
    var cpf: String,

    @field:Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}") // Phone format: (XX) XXXX-XXXX or (XX) XXXXX-XXXX
    @field:Size(min = 11, max = 15)
    var telefone: String? = null,

    @field:NotBlank
    @field:Email
    var email: String,

    @field:Past
    var dt_nasc: LocalDate,

    @field:NotBlank @field:Size(max = 125)
    var etnia: String,

    @field:NotBlank @field:Size(max = 125)
    var nacionalidade: String,

    @field:NotBlank @field:Size(max = 125)
    var genero: String,

    @field:NotBlank
    @field:Size(max = 14)
    var senha: String,

    @JsonIgnore
    var isAutenticado: Boolean = false,

    @Column(columnDefinition = "tinyint")
    @JsonIgnore
    var isAtivo: Boolean = true,

    @JsonIgnore
    var dtCadastro: LocalDate = LocalDate.now(),

    @JsonIgnore
    var dtDesativacao: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "fk_cargo_usuario")
    var cargo: Cargo,

    @ManyToOne
    @JoinColumn(name = "fk_endereco_usuario")
    var endereco: Endereco,

    @ManyToOne
    @JoinColumn(name = "fk_canal_comunicacao_usuario")
    var canalComunicacao: CanalComunicacao
)