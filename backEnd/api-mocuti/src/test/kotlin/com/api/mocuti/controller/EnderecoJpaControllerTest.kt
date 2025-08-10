package com.api.mocuti.controller

import com.api.mocuti.entity.Endereco
import com.api.mocuti.repository.EnderecoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class EnderecoJpaControllerTest {
    private val repositorio = mock(EnderecoRepository::class.java)
    private val controller = EnderecoJpaController(repositorio)

    private fun criarEnderecoValido() = Endereco(
        idEndereco = 1,
        cep = "12345678",
        logradouro = "Rua A",
        numero = 100,
        complemento = "Apto 1",
        uf = "SP",
        estado = "São Paulo",
        bairro = "Centro"
    )

    @Test
    @DisplayName("POST deve retornar 201 quando dados válidos")
    fun testPostEnderecoValido() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.save(endereco)).thenReturn(endereco)

        val response = controller.postEndereco(endereco)

        assertEquals(201, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    @DisplayName("GET deve retornar 204 quando lista vazia")
    fun testGetEnderecoVazio() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.getEndereco()

        assertEquals(204, response.statusCode.value())
    }

    @Test
    @DisplayName("GET deve retornar 200 com lista de endereços")
    fun testGetEnderecoComDados() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.findAll()).thenReturn(listOf(endereco))

        val response = controller.getEndereco()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(endereco, response.body?.first())
    }

    @Test
    @DisplayName("PUT deve retornar 404 quando id não existe")
    fun testPutEnderecoNaoExiste() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.existsById(1)).thenReturn(false)

        val response = controller.putEndereco(1, endereco)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    @DisplayName("PUT deve atualizar e retornar 200 quando id existe")
    fun testPutEnderecoExiste() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.save(any(Endereco::class.java))).thenReturn(endereco)

        val response = controller.putEndereco(1, endereco)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    @DisplayName("GET Endereco do Usuario deve retornar 404 quando não encontrado")
    fun testGetEnderecoDoUsuarioNaoEncontrado() {
        `when`(repositorio.findByUsuarioId(1)).thenReturn(null)

        val response = controller.getEnderecoDoUsuario(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    @DisplayName("GET Endereco do Usuario deve retornar 200 quando encontrado")
    fun testGetEnderecoDoUsuarioEncontrado() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.findByUsuarioId(1)).thenReturn(endereco)

        val response = controller.getEnderecoDoUsuario(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    @DisplayName("GET Endereco do Evento deve retornar 404 quando não encontrado")
    fun testGetEnderecoDoEventoNaoEncontrado() {
        `when`(repositorio.findEnderecoByEventoId(1)).thenReturn(null)

        val response = controller.getEnderecoDoEvento(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    @DisplayName("GET Endereco do Evento deve retornar 200 quando encontrado")
    fun testGetEnderecoDoEventoEncontrado() {
        val endereco = criarEnderecoValido()
        `when`(repositorio.findEnderecoByEventoId(1)).thenReturn(endereco)

        val response = controller.getEnderecoDoEvento(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }
}
