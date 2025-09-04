package com.api.mocuti.service

import com.api.mocuti.dto.UsuarioCadastroRequest
import com.api.mocuti.dto.UsuarioLoginRequest
import com.api.mocuti.dto.UsuarioRedefinirSenhaRequest
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
import com.api.mocuti.service.UsuarioService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class UsuarioServiceTest {

    @Mock
    lateinit var usuarioRepository: UsuarioRepository

    @Mock
    lateinit var cargoRepository: CargoRepository

    @Mock
    lateinit var enderecoRepository: EnderecoRepository

    @Mock
    lateinit var canalComunicacaoRepository: CanalComunicacaoRepository

    @InjectMocks
    lateinit var usuarioService: UsuarioService

    private lateinit var cargo: Cargo
    private lateinit var endereco: Endereco
    private lateinit var canal: CanalComunicacao
    private lateinit var usuario: Usuario

    @BeforeEach
    fun setup() {
        cargo = Cargo(1, "Cargo Teste")
        endereco = Endereco(1, "Rua X", "123", 5, "Cidade", "Estado", "São Paulo", "Morro doce")
        canal = CanalComunicacao(1, "Email")

        usuario = Usuario(
            idUsuario = 1,
            nomeCompleto = "Usuário Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "email@test.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileira",
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
    fun `deve cadastrar usuario com sucesso`() {
        val request = UsuarioCadastroRequest(
            nomeCompleto = "Novo Usuário",
            cpf = "123.456.789-00",
            telefone = "(11) 98888-8888",
            email = "novo@test.com",
            dataNascimento = LocalDate.of(1995, 5, 20),
            etnia = "Pardo",
            nacionalidade = "Brasileira",
            genero = "Feminino",
            senha = "senha123",
            cargo = 1,
            endereco = endereco,
            canalComunicacao = 1
        )

        `when`(usuarioRepository.existsByEmail(request.email)).thenReturn(false)
        `when`(usuarioRepository.existsByCpf(request.cpf)).thenReturn(false)
        `when`(cargoRepository.findById(1)).thenReturn(Optional.of(cargo))
        `when`(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco))
        `when`(canalComunicacaoRepository.findById(1)).thenReturn(Optional.of(canal))
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuario)

        val result = usuarioService.cadastrarUsuario(request)

        assertEquals("Usuário Teste", result.nomeCompleto)
        verify(usuarioRepository).save(any(Usuario::class.java))
    }

    @Test
    fun `deve lancar excecao ao cadastrar com email duplicado`() {
        val request = UsuarioCadastroRequest(
            "Nome", "123.456.789-00", "(11) 98888-8888",
            LocalDate.of(1995, 5, 20), "email@test.com", "Pardo",
            "Brasileira", "Feminino", "senha123", 1, endereco, 1
        )

        `when`(usuarioRepository.existsByEmail(request.email)).thenReturn(true)

        val ex = assertThrows<IllegalArgumentException> {
            usuarioService.cadastrarUsuario(request)
        }
        assertEquals("Email já cadastrado", ex.message)
    }

    @Test
    fun `deve autenticar usuario`() {
        val loginRequest = UsuarioLoginRequest("email@test.com", "123456")

        `when`(usuarioRepository.findByEmail(loginRequest.email)).thenReturn(usuario)
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuario.copy(isAutenticado = true))

        val result = usuarioService.autenticarUsuario(loginRequest)

        assertTrue(result.isAutenticado)
    }

    @Test
    fun `deve falhar autenticar com senha incorreta`() {
        val loginRequest = UsuarioLoginRequest("email@test.com", "errada")

        `when`(usuarioRepository.findByEmail(loginRequest.email)).thenReturn(usuario)

        val ex = assertThrows<IllegalArgumentException> {
            usuarioService.autenticarUsuario(loginRequest)
        }
        assertEquals("Senha incorreta", ex.message)
    }

    @Test
    fun `deve desautenticar usuario`() {
        val loginRequest = UsuarioLoginRequest("email@test.com", "123456")

        usuario.isAutenticado = true
        `when`(usuarioRepository.findByEmail(loginRequest.email)).thenReturn(usuario)
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenReturn(usuario.copy(isAutenticado = false))

        val result = usuarioService.desautenticarUsuario(loginRequest)

        assertFalse(result.isAutenticado)
    }

    @Test
    fun `deve redefinir senha`() {
        val request = UsuarioRedefinirSenhaRequest("novaSenha")

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        usuarioService.redefinirSenha(1, request)

        verify(usuarioRepository).save(argThat { request.senha == "novaSenha" })
    }

    @Test
    fun `deve desativar usuario`() {
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenAnswer { it.arguments[0] }

        val result = usuarioService.desativarUsuario(1)

        assertFalse(result.isAtivo)
        assertNotNull(result.dtDesativacao)
    }

    @Test
    fun `deve ativar usuario`() {
        usuario.isAtivo = false
        usuario.dtDesativacao = LocalDate.now()

        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))
        `when`(usuarioRepository.save(any(Usuario::class.java))).thenAnswer { it.arguments[0] }

        val result = usuarioService.ativarUsuario(1)

        assertTrue(result.isAtivo)
        assertNull(result.dtDesativacao)
    }
}
