package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Preferencia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_preferencia")
    var id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "fk_usuario_preferencia")
    var usuario: Usuario? = null,

    @ManyToOne
    @JoinColumn(name = "fk_categoria_preferencia")
    var categoria: Categoria? = null,
) {
}