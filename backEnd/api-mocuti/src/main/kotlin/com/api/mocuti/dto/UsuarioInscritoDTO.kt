package com.api.mocuti.dto

    data class UsuarioInscritoDTO(
        val idUsuario: Long,
        val nomeCompleto: String,
        val email: String,
        val tipoCargo: String,
        val nomeEvento: String,
        val isInscrito: Boolean,
        val isPresente: Boolean,
        val tipoInscricao: String
    )