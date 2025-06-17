package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
data class Endereco(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idEndereco: Int?, @field:NotNull
    @Column(columnDefinition = "char(8)")
    @field:Size(min = 8, max = 8) var CEP: String? = null,
    @field:NotBlank var logradouro: String? = null,
    @field:Min(1) var numero: Int = 0,
    @field:Size(max = 45) var complemento: String? = null,
    @Column(columnDefinition = "char(2)")
    @field:Size(min = 2, max = 2) var UF: String? = null,
    @field:Size(max = 45) var estado: String? = null,
    @field:Size(max = 125) var bairro: String? = null
) {
    constructor() : this(null, null)
}