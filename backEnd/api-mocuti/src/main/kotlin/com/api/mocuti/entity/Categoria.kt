package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity // do pacote jakarta.persistence
data class Categoria(
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categoria")
    var id: Int?,
    @field:NotBlank @field:Size(min = 2, max = 20) var nome: String?,
    @field:NotBlank var descricao: String?

) {
    constructor() : this(null, null, null)
}

