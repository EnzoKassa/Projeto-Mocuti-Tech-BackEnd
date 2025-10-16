package com.api.mocuti.service

import com.api.mocuti.entity.CanalComunicacao
import com.api.mocuti.entity.Cargo
import com.api.mocuti.entity.Endereco
import com.api.mocuti.entity.Usuario
import com.api.mocuti.repository.UsuarioRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class AuthServiceTestSimples {

    @Mock
    lateinit var usuarioRepository: UsuarioRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var emailService: EmailService

    @InjectMocks
    lateinit var authService: AuthService

    private lateinit var usuario: Usuario
    private lateinit var cargo: Cargo
    private lateinit var endereco: Endereco
    private lateinit var canal: CanalComunicacao

    @BeforeEach
    fun setup() {

        cargo = Cargo(1, "Cargo Teste")
        endereco = Endereco(1, "Rua X", "123", 5, "Cidade", "Estado", "São Paulo", "Bairro Y")
        canal = CanalComunicacao(1, "Email");

        usuario = Usuario(
            idUsuario = 1,
            nomeCompleto = "Usuário Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "email@test.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileiro",
            genero = "Masculino",
            senha = "123456",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargo,
            endereco = endereco,
            canalComunicacao = canal
        )
    }

    @Test
    fun `solicitarResetSenha lança excecao para email inexistente`() {
        `when`(usuarioRepository.findByEmail("invalido@test.com")).thenReturn(null)

        val ex = assertThrows<IllegalArgumentException> {
            authService.solicitarResetSenha("invalido@test.com")
        }

        assert(ex.message == "Usuário não encontrado")
    }

    @Test
    fun `redefinirSenha atualiza senha`() {
        `when`(usuarioRepository.findByEmail(usuario.email)).thenReturn(usuario)
        `when`(passwordEncoder.encode("novaSenha")).thenReturn("senhaCriptografada")

        // gera token
        authService.solicitarResetSenha(usuario.email)
        val token = authService.javaClass.getDeclaredField("tokensResetSenha").let {
            it.isAccessible = true
            val map = it.get(authService) as Map<String, String>
            map.keys.first()
        }

        authService.redefinirSenha(token, "novaSenha")

        assert(usuario.senha == "senhaCriptografada")
    }

    @Test
    fun `redefinirSenha lança excecao para token invalido`() {
        val ex = assertThrows<IllegalArgumentException> {
            authService.redefinirSenha("000000", "novaSenha")
        }

        assert(ex.message == "Token inválido ou expirado")
    }
}
