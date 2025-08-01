package com.api.mocuti.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioRedefinirSenhaRequest(
    @field:NotBlank
    @field:Size(max = 14)
    val senha: String
)
