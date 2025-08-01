package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Preferencia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idPreferencia: Int,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_preferencia")
    var usuario: Usuario,

    @ManyToOne
    @JoinColumn(name = "fk_categoria_preferencia")
    var categoria: Categoria
)