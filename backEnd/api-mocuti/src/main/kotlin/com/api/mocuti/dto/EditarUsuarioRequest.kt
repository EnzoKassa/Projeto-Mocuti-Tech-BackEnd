package com.api.mocuti.dto

data class EditarUsuarioRequest(
    val nomeCompleto: String,
    val cpf: String,
    val telefone: String,
    val email: String,
    val dt_nasc: String,
    val etnia: String,
    val nacionalidade: String,
    val genero: String
)
