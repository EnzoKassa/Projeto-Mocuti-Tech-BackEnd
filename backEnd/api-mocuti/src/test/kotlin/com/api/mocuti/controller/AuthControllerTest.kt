package com.api.mocuti.controller

import com.api.mocuti.dto.RedefinirSenhaRequest
import com.api.mocuti.dto.ResetRequest
import com.api.mocuti.service.AuthService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class AuthControllerTest {

    @Mock
    lateinit var authService: AuthService

    @InjectMocks
    lateinit var controller: AuthController

    @Test
    fun `POST forgot-password deve retornar 200 e mensagem de sucesso`() {
        val request = ResetRequest(email = "teste@email.com")

        // Act
        val resposta = controller.forgotPassword(request)

        // Assert
        assertEquals(HttpStatus.OK, resposta.statusCode)
        assertEquals("E-mail de recuperação enviado", resposta.body)
        verify(authService, times(1)).solicitarResetSenha("teste@email.com")
    }

    @Test
    fun `POST reset-password deve retornar 200 e mensagem de sucesso`() {
        val request = RedefinirSenhaRequest(
            token = "abc123",
            novaSenha = "novaSenhaSegura"
        )

        // Act
        val resposta = controller.resetPassword(request)

        // Assert
        assertEquals(HttpStatus.OK, resposta.statusCode)
        assertEquals("Senha redefinida com sucesso", resposta.body)
        verify(authService, times(1)).redefinirSenha("abc123", "novaSenhaSegura")
    }
}