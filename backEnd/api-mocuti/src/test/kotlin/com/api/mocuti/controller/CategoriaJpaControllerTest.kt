package com.api.mocuti.controller

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Categoria
import com.api.mocuti.service.CategoriaService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class CategoriaJpaControllerTest {

    private val service = mock(CategoriaService::class.java)
    private val controller = CategoriaJpaController(service)

    private val categoriaExistente = Categoria(1, "Eletrônicos", "Produtos tecnológicos")

    @Test
    fun `GET com dados deve retornar 200 com lista`() {
        `when`(service.listarTodos()).thenReturn(listOf(categoriaExistente))

        val resposta = controller.get()

        assertEquals(200, resposta.statusCode.value())
        assertEquals(1, resposta.body?.size)
        assertEquals("Eletrônicos", resposta.body?.first()?.nome)
    }

    @Test
    fun `GET sem dados deve retornar 204 e corpo nulo`() {
        `when`(service.listarTodos()).thenReturn(emptyList())

        val resposta = controller.get()

        assertEquals(204, resposta.statusCode.value())
        assertNull(resposta.body)
    }

    @Test
    fun `GET por ID existente deve retornar 200 com categoria`() {
        `when`(service.buscarPorId(1)).thenReturn(categoriaExistente)

        val resposta = controller.get(1)

        assertEquals(200, resposta.statusCode.value())
        assertEquals(categoriaExistente, resposta.body)
    }

    @Test
    fun `GET por ID inexistente deve retornar 404 com corpo nulo`() {
        `when`(service.buscarPorId(9)).thenReturn(null)

        val resposta = controller.get(9)

        assertEquals(404, resposta.statusCode.value())
        assertNull(resposta.body)
    }

    @Test
    fun `DELETE por ID existente deve retornar 204`() {
        `when`(service.deletar(1)).thenReturn(true)

        val resposta = controller.delete(1)

        assertEquals(204, resposta.statusCode.value())
        verify(service, times(1)).deletar(1)
    }

    @Test
    fun `DELETE por ID inexistente deve retornar 404`() {
        `when`(service.deletar(9)).thenReturn(false)

        val resposta = controller.delete(9)

        assertEquals(404, resposta.statusCode.value())
        verify(service, times(1)).deletar(9)
    }

    @Test
    fun `POST deve salvar nova categoria e retornar 201`() {
        val novaCategoria = Categoria(0, "Jogos", "Coletivos de jogos")
        val salva = Categoria(1, "Jogos", "Coletivos de jogos")

        `when`(service.salvar(novaCategoria)).thenReturn(salva)

        val resposta = controller.post(novaCategoria)

        assertEquals(201, resposta.statusCode.value())
        assertEquals("Jogos", resposta.body?.nome)
        verify(service, times(1)).salvar(novaCategoria)
    }

    @Test
    fun `PUT com ID existente deve atualizar e retornar 200`() {
        val atualizada = Categoria(0, "Atualizada", "Categoria atualizada")
        val salva = Categoria(1, "Atualizada", "Categoria atualizada")

        `when`(service.atualizar(1, atualizada)).thenReturn(salva)

        val resposta = controller.put(1, atualizada)

        assertEquals(200, resposta.statusCode.value())
        assertEquals("Atualizada", resposta.body?.nome)
        verify(service, times(1)).atualizar(1, atualizada)
    }

    @Test
    fun `PUT com ID inexistente deve retornar 404`() {
        val atualizada = Categoria(0, "Atualizada", "Categoria atualizada")

        `when`(service.atualizar(9, atualizada)).thenReturn(null)

        val resposta = controller.put(9, atualizada)

        assertEquals(404, resposta.statusCode.value())
        assertNull(resposta.body)
        verify(service, times(1)).atualizar(9, atualizada)
    }

    @Test
    fun `GET ranking deve retornar 200 com lista`() {
        val ranking = listOf(
            object : RankCategoriaRequest {
                override val id_categoria: Int = 1
                override val nome: String = "Eletrônicos"
                override val descricao: String = "Produtos eletrônicos"
                override val total_votos: Int = 5
            },
            object : RankCategoriaRequest {
                override val id_categoria: Int = 2
                override val nome: String = "Jogos"
                override val descricao: String = "Jogos e afins"
                override val total_votos: Int = 3
            }
        )

        `when`(service.getRanking()).thenReturn(ranking)

        val resposta = controller.getRanking()

        assertEquals(200, resposta.statusCode.value())
        assertEquals(2, resposta.body?.size)
        assertEquals("Eletrônicos", resposta.body?.first()?.nome)
        assertEquals(5, resposta.body?.first()?.total_votos)
        verify(service, times(1)).getRanking()
    }

}
