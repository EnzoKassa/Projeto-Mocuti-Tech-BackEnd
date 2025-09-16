package com.api.mocuti.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
data class Preferencia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idPreferencia: Int? = null, // deixar null pro JPA preencher

    @ManyToOne(fetch = FetchType.LAZY) // Lazy evita carregar tudo de cara
    @JoinColumn(name = "fk_usuario_preferencia", nullable = false)
    var usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_categoria_preferencia", nullable = false)
    var categoria: Categoria
)