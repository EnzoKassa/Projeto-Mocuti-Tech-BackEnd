package com.api.mocuti.controller

import com.api.mocuti.dto.RedefinirSenhaRequest
import com.api.mocuti.dto.ResetRequest
import com.api.mocuti.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(
        summary = "Solicitar redefinição de senha",
        description = "Envia um e-mail com instruções para redefinir a senha do usuário"
    )
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody request: ResetRequest): ResponseEntity<String> {
        authService.solicitarResetSenha(request.email)
        return ResponseEntity.ok("E-mail de recuperação enviado")
    }

    @Operation(
        summary = "Redefinir senha",
        description = "Redefine a senha do usuário utilizando o token enviado por e-mail"
    )
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: RedefinirSenhaRequest): ResponseEntity<String> {
        authService.redefinirSenha(request.token, request.novaSenha)
        return ResponseEntity.ok("Senha redefinida com sucesso")
    }
}   