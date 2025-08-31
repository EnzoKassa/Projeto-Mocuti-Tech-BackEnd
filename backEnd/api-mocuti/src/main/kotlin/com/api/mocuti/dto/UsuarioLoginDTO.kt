package com.api.mocuti.dto

import com.api.mocuti.entity.Cargo

data class UsuarioLoginDTO(
    val idUsuario: Long,
    val nomeCompleto: String,
    val email: String,
    val cargo: Cargo
)