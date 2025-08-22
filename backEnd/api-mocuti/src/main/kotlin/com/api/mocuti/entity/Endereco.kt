package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.*

@Entity
data class Endereco(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idEndereco: Int,

    @field:NotNull
    @field:Size(min = 8, max = 9)
    @field:Pattern(regexp = "\\d{5}-\\d{3}") // CPF format: 000000-000
    var cep: String,

    var logradouro: String,

    @field:Min(1)
    var numero: Int,

    @field:Size(max = 225)
    var complemento: String?,

    @field:Size(min = 2, max = 2)
    var uf: String,

    @field:NotBlank
    var estado: String,

    @field:NotBlank
    @field:Size(max = 125)
    var bairro: String
)