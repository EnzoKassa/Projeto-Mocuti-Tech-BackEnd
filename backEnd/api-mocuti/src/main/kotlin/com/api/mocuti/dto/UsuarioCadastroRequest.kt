package com.api.mocuti.dto

import com.api.mocuti.entity.Endereco
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class UsuarioCadastroRequest(
    val nomeCompleto: String,
    val cpf: String,
    val telefone: String?,
    val dataNascimento: LocalDate,
    val etnia: String,
    val nacionalidade: String,
    val genero: String,
    val email: String,
    val senha: String,
    val cargo: Int?,
    val endereco: Endereco,
    val canalComunicacao: Int,
    val idCategoriaPreferida: Int // agora é opcional

)