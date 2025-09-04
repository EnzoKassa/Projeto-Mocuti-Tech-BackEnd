package com.api.mocuti.controller

import com.api.mocuti.entity.Categoria
import com.api.mocuti.repository.CategoriaRepository
import com.api.mocuti.service.CategoriaService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class CategoriaJpaControllerTest {

    val repository = mock(CategoriaRepository::class.java)
    private val service = mock(CategoriaService::class.java)

    val controller = CategoriaJpaController(repository, service)

    val categoriaExistente = Categoria(1, "sdfgh",  "Eletrônicos")

    @BeforeEach
    fun setup() {
        `when`(repository.existsById(1)).thenReturn(true)
        `when`(repository.findById(1)).thenReturn(Optional.of(categoriaExistente))

        `when`(repository.existsById(9)).thenReturn(false)
        `when`(repository.findById(9)).thenReturn(Optional.empty())
    }

    @Test
    fun `GET com dados deve retornar 200 com lista`() {
        `when`(repository.findAll()).thenReturn(listOf(categoriaExistente))

        val resposta = controller.get()

        assertEquals(200, resposta.statusCode.value())
        assertEquals(1, resposta.body?.size)
    }

    @Test
    fun `GET sem dados deve retornar 204 e corpo nulo`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val resposta = controller.get()

        assertEquals(204, resposta.statusCode.value())
        assertNull(resposta.body)
    }

    @Test
    fun `GET por ID existente deve retornar 200 com categoria`() {
        val resposta = controller.get(1)

        assertEquals(200, resposta.statusCode.value())
        assertEquals(categoriaExistente, resposta.body)
    }

    @Test
    fun `GET por ID inexistente deve retornar 404 com corpo nulo`() {
        val resposta = controller.get(9)

        assertEquals(404, resposta.statusCode.value())
        assertNull(resposta.body)
    }

    @Test
    fun `DELETE por ID existente deve retornar 204 e chamar deleteById`() {
        val resposta = controller.delete(1)

        assertEquals(204, resposta.statusCode.value())
        assertNull(resposta.body)

        verify(repository, times(1)).deleteById(1)
    }

    @Test
    fun `DELETE por ID inexistente deve retornar 404 e NÃO chamar deleteById`() {
        val resposta = controller.delete(9)

        assertEquals(404, resposta.statusCode.value())
        assertNull(resposta.body)

        verify(repository, times(0)).deleteById(9)
    }

    @Test
    fun `POST deve salvar nova categoria e retornar 201`() {
        val novaCategoria = Categoria(0, "Jogos","coletivos de jogos")
        val salva = Categoria(1,"Jogos", "Coletivos de jogos")

        `when`(repository.save(novaCategoria)).thenReturn(salva)

        val resposta = controller.post(novaCategoria)

        assertEquals(201, resposta.statusCode.value())
        assertEquals("Jogos", resposta.body?.nome)
    }

    @Test
    fun `PUT com ID existente deve atualizar e retornar 200`() {
        val atualizada = Categoria(0, "Atualizada", "categoria atualizada")
        val salva = Categoria(1, "Atualizada", "categoria atualizada")

        `when`(repository.save(any(Categoria::class.java))).thenReturn(salva)

        val resposta = controller.put(1, atualizada)

        assertEquals(200, resposta.statusCode.value())
        assertEquals("Atualizada", resposta.body?.nome)
    }

    @Test
    fun `PUT com ID inexistente deve retornar 404`() {
        val atualizada = Categoria(0, "sdfgh", "Atualizada")

        val resposta = controller.put(9, atualizada)

        assertEquals(404, resposta.statusCode.value())
        assertNull(resposta.body)
    }
}
