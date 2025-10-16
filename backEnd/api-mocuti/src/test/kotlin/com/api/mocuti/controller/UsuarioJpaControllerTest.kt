package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.*
import com.api.mocuti.service.UsuarioService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

class UsuarioJpaControllerTest {

    private val usuarioService: UsuarioService = mock(UsuarioService::class.java)
    private val controller = UsuarioJpaController(usuarioService)

    private lateinit var cargoTeste: Cargo
    private lateinit var enderecoTeste: Endereco
    private lateinit var comunicacaoTeste: CanalComunicacao
    private lateinit var usuarioTeste: Usuario

    @BeforeEach
    fun setup() {
        cargoTeste = Cargo(idCargo = 1, tipoCargo = "Administrador")
        enderecoTeste = Endereco(
            idEndereco = 1,
            cep = "12345678",
            logradouro = "Rua das Flores",
            numero = 123,
            complemento = "Apto 45",
            uf = "SP",
            estado = "São Paulo",
            bairro = "Centro"
        )
        comunicacaoTeste = CanalComunicacao(idCanalComunicacao = 1, tipoCanalComunicacao = "Email")
        usuarioTeste = Usuario(
            idUsuario = 1,
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "teste@email.com",
            dt_nasc = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileira",
            genero = "Masculino",
            senha = "senha123",
            isAutenticado = false,
            isAtivo = true,
            dtDesativacao = null,
            cargo = cargoTeste,
            endereco = enderecoTeste,
            canalComunicacao = comunicacaoTeste
        )
    }

    // ----------- TESTES DE LISTAGEM -----------

    @Test
    fun `deve listar todos os usuarios`() {
        `when`(usuarioService.listarTodos()).thenReturn(listOf(usuarioTeste))

        val response = controller.listarTodos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `deve retornar no content se nao houver usuarios`() {
        `when`(usuarioService.listarTodos()).thenReturn(emptyList())

        val response = controller.listarTodos()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    // ----------- CADASTRO -----------

    @Test
    fun `deve cadastrar usuario com sucesso`() {
        val request = UsuarioCadastroRequest(
            nomeCompleto = "Teste",
            cpf = "123.456.789-00",
            telefone = "(11) 99999-9999",
            email = "teste@email.com",
            dataNascimento = LocalDate.of(1990, 1, 1),
            etnia = "Branco",
            nacionalidade = "Brasileira",
            genero = "Masculino",
            senha = "senha123",
            cargo = 1,
            endereco = enderecoTeste,
            canalComunicacao = 1
        )

        `when`(usuarioService.cadastrarUsuario(request)).thenReturn(usuarioTeste)

        val response = controller.cadastrar(request)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    // ----------- LOGIN / LOGOUT -----------

    @Test
    fun `deve logar com sucesso`() {
        val loginRequest = UsuarioLoginRequest(email = "teste@email.com", senha = "senha123")
        `when`(usuarioService.autenticarUsuario(loginRequest)).thenReturn(usuarioTeste)

        val response = controller.logar(loginRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste.email, response.body?.email)
        assertEquals(usuarioTeste.nomeCompleto, response.body?.nomeCompleto)
    }

    @Test
    fun `deve deslogar com sucesso`() {
        val loginRequest = UsuarioLoginRequest(email = "teste@email.com", senha = "senha123")
        `when`(usuarioService.desautenticarUsuario(loginRequest)).thenReturn(usuarioTeste)

        val response = controller.deslogar(loginRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    // ----------- SENHA -----------

    @Test
    fun `deve redefinir senha com sucesso`() {
        val request = UsuarioRedefinirSenhaRequest(senha = "novaSenha123")
        doNothing().`when`(usuarioService).redefinirSenha(usuarioTeste.idUsuario, request)

        val response = controller.redefinirSenha(usuarioTeste.idUsuario, request)

        assertEquals(HttpStatus.OK, response.statusCode)
        verify(usuarioService).redefinirSenha(usuarioTeste.idUsuario, request)
    }

    // ----------- ATIVAR / DESATIVAR -----------

    @Test
    fun `deve desativar usuario com sucesso`() {
        `when`(usuarioService.desativarUsuario(usuarioTeste.idUsuario)).thenReturn(usuarioTeste)

        val response = controller.desativarUsuario(usuarioTeste.idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        verify(usuarioService).desativarUsuario(usuarioTeste.idUsuario)
    }

    @Test
    fun `deve ativar usuario com sucesso`() {
        `when`(usuarioService.ativarUsuario(usuarioTeste.idUsuario)).thenReturn(usuarioTeste)

        val response = controller.ativarUsuario(usuarioTeste.idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        verify(usuarioService).ativarUsuario(usuarioTeste.idUsuario)
    }

    // ----------- BUSCAR POR ID -----------

    @Test
    fun `deve listar um usuario por id`() {
        `when`(usuarioService.buscarUsuarioPorId(usuarioTeste.idUsuario)).thenReturn(usuarioTeste)

        val response = controller.listarUmUsuario(usuarioTeste.idUsuario)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(usuarioTeste, response.body)
    }

    @Test
    fun `deve retornar not found se usuario nao existir`() {
        `when`(usuarioService.buscarUsuarioPorId(999)).thenThrow(NoSuchElementException())

        val response = controller.listarUmUsuario(999)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    // ----------- EDIÇÃO -----------

    @Test
    fun `deve editar usuario com sucesso`() {
        val editarRequest = EditarUsuarioRequest(
            nomeCompleto = "Atualizado",
            cpf = "000.000.000-00",
            telefone = "(11) 98888-7777",
            email = "novo@email.com",
            dt_nasc = "2000-01-01",
            etnia = "Parda",
            nacionalidade = "Brasileira",
            genero = "Feminino"
        )

        val usuarioAtualizado = usuarioTeste.copy(
            nomeCompleto = "Atualizado",
            telefone = "(11) 98888-7777",
            email = "novo@email.com"
        )

        `when`(usuarioService.editarUsuario(usuarioTeste.idUsuario.toLong(), editarRequest))
            .thenReturn(usuarioAtualizado)

        val response = controller.editarUsuario(usuarioTeste.idUsuario.toLong(), editarRequest)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Atualizado", response.body?.nomeCompleto)
    }
}
