package com.api.mocuti.controller

import com.api.mocuti.dto.FeedbackAtualizarRequest
import com.api.mocuti.dto.FeedbackNovoRequest
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
import com.api.mocuti.service.FeedbackService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class FeedbackJpaControllerTest {

    private val feedbackRepository: FeedbackRepository = mock(FeedbackRepository::class.java)
    private val usuarioRepository: UsuarioRepository = mock(UsuarioRepository::class.java)
    private val eventoRepository: EventoRepository = mock(EventoRepository::class.java)
    private val notaFeedbackRepository: NotaFeedbackRepository = mock(NotaFeedbackRepository::class.java)
    private val service: FeedbackService = mock(FeedbackService::class.java)

    private val controller = FeedbackJpaController(
        feedbackRepository,
        eventoRepository,
        usuarioRepository,
        notaFeedbackRepository,
        service
    )

    @Test
    fun `deve retornar 200 e lista de feedbacks quando existirem feedbacks`() {
        val feedbacks = listOf(mock(Feedback::class.java))
        `when`(feedbackRepository.findAll()).thenReturn(feedbacks)

        val response = controller.getFeedback()

        assertEquals(200, response.statusCode.value())
        assertEquals(feedbacks, response.body)
    }

    @Test
    fun `deve retornar 204 quando nao houver nenhum feedback`() {
        `when`(feedbackRepository.findAll()).thenReturn(emptyList())

        val response = controller.getFeedback()

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 200 e o feedback quando o ID existir`() {
        val feedback = mock(Feedback::class.java)
        `when`(feedbackRepository.existsById(1)).thenReturn(true)
        `when`(feedbackRepository.findById(1)).thenReturn(Optional.of(feedback))

        val response = controller.getFeedbackPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedback, response.body)
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir`() {
        `when`(feedbackRepository.existsById(1)).thenReturn(false)

        val response = controller.getFeedbackPorId(1)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 204 e deletar o feedback quando o ID existir`() {
        `when`(feedbackRepository.existsById(1)).thenReturn(true)

        val response = controller.deleteFeedback(1)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
        verify(feedbackRepository).deleteById(1)
    }

    @Test
    fun `deve retornar 404 quando o ID nao existir ao deletar`() {
        `when`(feedbackRepository.existsById(1)).thenReturn(false)

        val response = controller.deleteFeedback(1)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deve retornar 201 e o feedback criado quando os dados forem validos`() {
        val dto = mock(FeedbackNovoRequest::class.java)
        val feedback = mock(Feedback::class.java)

        `when`(service.criar(dto)).thenReturn(feedback)

        val response = controller.postFeedback(dto)

        assertEquals(201, response.statusCode.value())
        assertEquals(feedback, response.body)
    }

    @Test
    fun `deve retornar 200 e atualizar o feedback quando o ID existir`() {
        val dto = mock(FeedbackAtualizarRequest::class.java)
        val feedbackAtualizado = mock(Feedback::class.java)

        `when`(service.atualizar(1, dto)).thenReturn(feedbackAtualizado)

        val response = controller.putFeedback(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(feedbackAtualizado, response.body)
    }
}
