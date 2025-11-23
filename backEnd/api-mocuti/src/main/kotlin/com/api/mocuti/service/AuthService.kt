package com.api.mocuti.service

import com.api.mocuti.repository.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) {
    // Mapa temporário para armazenar tokens (token -> email)
    private val tokensResetSenha = ConcurrentHashMap<String, String>()

    fun solicitarResetSenha(email: String) {
        val usuario = usuarioRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Usuário não encontrado")

        val token = (100000..999999).random().toString().trim() // token de 6 dígitos
        tokensResetSenha[token] = email

        emailService.enviarEmailResetSenha(email, token)
    }

    fun redefinirSenha(token: String, novaSenha: String) {
        val tokenLimpo = token.trim()

        val email = tokensResetSenha[tokenLimpo]
            ?: throw IllegalArgumentException("Token inválido ou expirado")

        val usuario = usuarioRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Usuário não encontrado")

        // Verifica se a nova senha é igual à senha atual
        if (passwordEncoder.matches(novaSenha, usuario.senha)) {
            throw IllegalArgumentException("A nova senha não pode ser igual à anterior.")
        }

        // Atualiza com senha nova criptografada
        usuario.senha = passwordEncoder.encode(novaSenha)
        usuarioRepository.save(usuario)

        // Remove token após uso
        tokensResetSenha.remove(tokenLimpo)
    }

}
