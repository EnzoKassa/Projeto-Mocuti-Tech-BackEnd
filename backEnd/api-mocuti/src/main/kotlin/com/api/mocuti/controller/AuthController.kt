package com.api.mocuti.controller

import com.api.mocuti.dto.RedefinirSenhaRequest
import com.api.mocuti.dto.ResetRequest
import com.api.mocuti.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody request: ResetRequest): ResponseEntity<String> {
        authService.solicitarResetSenha(request.email)
        return ResponseEntity.ok("E-mail de recuperação enviado")
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: RedefinirSenhaRequest): ResponseEntity<String> {
        authService.redefinirSenha(request.token, request.novaSenha)
        return ResponseEntity.ok("Senha redefinida com sucesso")
    }
}