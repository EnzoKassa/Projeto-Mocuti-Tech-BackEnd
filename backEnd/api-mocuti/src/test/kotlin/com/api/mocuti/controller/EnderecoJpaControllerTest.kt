package com.api.mocuti.controller

import com.api.mocuti.entity.Endereco
import com.api.mocuti.repository.EnderecoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.Test

class EnderecoJpaControllerTest{
    private val repositorio = mock(EnderecoRepository::class.java)
    private val controller = EnderecoJpaController(repositorio)


    @Test
    @DisplayName("POST deve retornar 201 quando dados válidos")
    fun testPostEnderecoValido() {
        val endereco = Endereco(idEndereco = 1, CEP = "12345678")
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
    @DisplayName("PUT deve retornar 404 quando id não existe")
    fun testPutEnderecoNaoExiste() {
        val endereco = Endereco(idEndereco = 1, CEP = "12345678")
        `when`(repositorio.existsById(1)).thenReturn(false)

        val response = controller.putEndereco(1, endereco)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    @DisplayName("PUT deve atualizar e retornar 200 quando id existe")
    fun testPutEnderecoExiste() {
        val endereco = Endereco(idEndereco = 1, CEP = "12345678")
        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.save(endereco)).thenReturn(endereco)

        val response = controller.putEndereco(1, endereco)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    @DisplayName("GET Endereco do Usuario deve retornar 204 quando não encontrado")
    fun testGetEnderecoDoUsuarioNaoEncontrado() {
        `when`(repositorio.findEnderecoByUsuarioId(1)).thenReturn(null)

        val response = controller.getEnderecoDoUsuario(1)

        assertEquals(204, response.statusCode.value())
    }

    @Test
    @DisplayName("GET Endereco do Usuario deve retornar 200 quando encontrado")
    fun testGetEnderecoDoUsuarioEncontrado() {
        val endereco = Endereco(idEndereco = 1, CEP = "12345678")
        `when`(repositorio.findEnderecoByUsuarioId(1)).thenReturn(endereco)

        val response = controller.getEnderecoDoUsuario(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    @DisplayName("GET Endereco do Evento deve retornar 204 quando não encontrado")
    fun testGetEnderecoDoEventoNaoEncontrado() {
        `when`(repositorio.findEnderecoByEventoId(1)).thenReturn(null)

        val response = controller.getEnderecoDoEvento(1)

        assertEquals(204, response.statusCode.value())
    }

    @Test
    @DisplayName("GET Endereco do Evento deve retornar 200 quando encontrado")
    fun testGetEnderecoDoEventoEncontrado() {
        val endereco = Endereco(idEndereco = 1, CEP = "12345678")
        `when`(repositorio.findEnderecoByEventoId(1)).thenReturn(endereco)

        val response = controller.getEnderecoDoEvento(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(endereco, response.body)
    }
}