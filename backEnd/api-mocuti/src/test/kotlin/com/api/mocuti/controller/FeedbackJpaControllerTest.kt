package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.Feedback
import com.api.mocuti.service.FeedbackService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class FeedbackJpaControllerTest {

    private val service: FeedbackService = mock(FeedbackService::class.java)
    private val controller = FeedbackJpaController(service)

    @Test
    fun `getFeedback deve retornar 200 e lista de feedbacks quando existirem`() {
        val feedbacks = listOf(mock(Feedback::class.java))
        `when`(service.listarTodos()).thenReturn(feedbacks)

        val response = controller.getFeedback()

        assertEquals(200, response.statusCode.value())
        assertEquals(feedbacks, response.body)
        verify(service, times(1)).listarTodos()
    }

    @Test
    fun `getFeedback deve retornar 204 quando lista estiver vazia`() {
        `when`(service.listarTodos()).thenReturn(emptyList())

        val response = controller.getFeedback()

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
        verify(service, times(1)).listarTodos()
    }

    @Test
    fun `getFeedbackPorId deve retornar 200 quando feedback existir`() {
        val feedback = mock(Feedback::class.java)
        `when`(service.buscarPorId(1)).thenReturn(feedback)

        val response = controller.getFeedbackPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedback, response.body)
        verify(service, times(1)).buscarPorId(1)
    }

    @Test
    fun `getFeedbackPorId deve retornar 404 quando feedback nao existir`() {
        `when`(service.buscarPorId(1)).thenReturn(null)

        val response = controller.getFeedbackPorId(1)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
        verify(service, times(1)).buscarPorId(1)
    }

    @Test
    fun `deleteFeedback deve retornar 204 quando feedback for deletado`() {
        `when`(service.deletar(1)).thenReturn(true)

        val response = controller.deleteFeedback(1)

        assertEquals(204, response.statusCode.value())
        verify(service, times(1)).deletar(1)
    }

    @Test
    fun `deleteFeedback deve retornar 404 quando feedback nao existir`() {
        `when`(service.deletar(1)).thenReturn(false)

        val response = controller.deleteFeedback(1)

        assertEquals(404, response.statusCode.value())
        verify(service, times(1)).deletar(1)
    }

    @Test
    fun `postFeedback deve retornar 200 e feedback criado ou atualizado`() {
        val dto = mock(FeedbackNovoRequest::class.java)
        val feedback = mock(Feedback::class.java)
        `when`(service.criarOuAtualizar(dto)).thenReturn(feedback)

        val response = controller.postFeedback(dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedback, response.body)
        verify(service, times(1)).criarOuAtualizar(dto)
    }

    @Test
    fun `getFeedbackCategoria deve retornar 200 e lista de feedbacks por categoria`() {
        val lista = listOf(mock(FeedbacksPorCategoriaRequest::class.java))
        `when`(service.getFeedbackPorCategoria()).thenReturn(lista)

        val response = controller.getFeedbackCategoria()

        assertEquals(200, response.statusCode.value())
        assertEquals(lista, response.body)
        verify(service, times(1)).getFeedbackPorCategoria()
    }

    @Test
    fun `getFeedbackCategoriaMesAtual deve retornar 200 e lista de feedbacks`() {
        val lista = listOf(mock(FeedbackCategoriaMesAtualRequest::class.java))
        `when`(service.getFeedbackCategoriaMesAtual()).thenReturn(lista)

        val response = controller.getFeedbackCategoriaMesAtual()

        assertEquals(200, response.statusCode.value())
        assertEquals(lista, response.body)
        verify(service, times(1)).getFeedbackCategoriaMesAtual()
    }

    @Test
    fun `getFeedbackEvento deve retornar 200 e lista de feedbacks por evento`() {
        val lista = listOf(mock(FeedbackEventoRequest::class.java))
        `when`(service.getFeedbackEvento()).thenReturn(lista)

        val response = controller.getFeedbackEvento()

        assertEquals(200, response.statusCode.value())
        assertEquals(lista, response.body)
        verify(service, times(1)).getFeedbackEvento()
    }
}
