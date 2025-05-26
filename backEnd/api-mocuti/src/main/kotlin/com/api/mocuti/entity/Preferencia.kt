package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Preferencia (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "preferencia_usuario")
    var usuario: Usuario? = null,

    @ManyToOne
    @JoinColumn(name = "preferencia_categoria")
    var categoria: Categoria? = null,
) {
}