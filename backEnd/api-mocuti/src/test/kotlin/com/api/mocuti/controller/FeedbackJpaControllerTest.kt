package com.api.mocuti.controller

import com.api.mocuti.entity.Feedback
import com.api.mocuti.repository.FeedbackRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class FeedbackJpaControllerTest {

    private val repositorio: FeedbackRepository = mock(FeedbackRepository::class.java)
    private val controller = FeedbackJpaController(repositorio)

    @Test
    fun `deve retornar 200 e lista de feedbacks quando existirem feedbacks`() {
        val feedbacks = listOf(
            Feedback(id = 1, comentario = "Ótimo serviço"),
            Feedback(id = 2, comentario = "Muito bom")
        )
        `when`(repositorio.findAll()).thenReturn(feedbacks)

        val response = controller.get()

        assertEquals(200, response.statusCode.value())
        assertEquals(feedbacks, response.body)
    }

    @Test
    fun `deve retornar 204 quando nao houver nenhum feedback`() {
        `when`(repositorio.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 200 e o feedback quando o ID existir`() {
        val feedback = Feedback(id = 1, comentario = "Ótimo serviço")
        `when`(repositorio.findById(1)).thenReturn(java.util.Optional.of(feedback))

        val response = controller.get(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedback, response.body)
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir`() {
        `when`(repositorio.findById(1)).thenReturn(java.util.Optional.empty())

        val response = controller.get(1)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 204 e deletar o feedback quando o ID existir`() {
        `when`(repositorio.existsById(1)).thenReturn(true)

        val response = controller.delete(1)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
        verify(repositorio, times(1)).deleteById(1)
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir ao deletar`() {
        `when`(repositorio.existsById(1)).thenReturn(false)

        val response = controller.delete(1)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 201 e o feedback criado quando os dados forem validos`() {
        val feedback = Feedback(id = 1, comentario = "Ótimo serviço")
        `when`(repositorio.save(any(Feedback::class.java))).thenReturn(feedback)

        val response = controller.post(feedback)

        assertEquals(201, response.statusCode.value())
        assertEquals(feedback, response.body)
    }

    @Test
    fun `deve retornar 200 e atualizar o feedback quando o ID existir`() {
        val feedbackAtualizado = Feedback(id = 1, comentario = "Serviço atualizado")
        `when`(repositorio.existsById(1)).thenReturn(true)
        `when`(repositorio.save(any(Feedback::class.java))).thenReturn(feedbackAtualizado)

        val response = controller.put(1, feedbackAtualizado)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedbackAtualizado, response.body)
    }

    @Test
    fun `deve retornar 404 ao tentar atualizar um ID inexistente`() {
        val feedbackAtualizado = Feedback(id = 1, comentario = "Serviço atualizado")
        `when`(repositorio.existsById(1)).thenReturn(false)

        val response = controller.put(1, feedbackAtualizado)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 404 ao atualizar feedback com dados invalidos`() {
        val feedbackInvalido = Feedback(id = 1, comentario = "")

        val response = controller.put(1, feedbackInvalido)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
        verify(repositorio, times(0)).save(any(Feedback::class.java))
    }
}