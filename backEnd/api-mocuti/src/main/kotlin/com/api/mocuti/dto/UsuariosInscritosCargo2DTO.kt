package com.api.mocuti.dto

data class UsuariosInscritosCargo2DTO(
    val idEvento: Int,
    val idUsuario: Int,
    val nomeCompleto: String,
    val email: String?,
    val telefone: String?,
    val tipoCargo: String,
    val nomeEvento: String,
    val isInscrito: Boolean,
    val isPresente: Boolean,
    val tipoInscricao: String
)