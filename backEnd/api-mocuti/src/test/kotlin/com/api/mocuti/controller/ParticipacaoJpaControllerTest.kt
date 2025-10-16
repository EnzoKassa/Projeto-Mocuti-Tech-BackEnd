package com.api.mocuti.controller

import com.api.mocuti.dto.ParticipacaoFeedbackDTO
import com.api.mocuti.entity.Evento
import com.api.mocuti.entity.NotaFeedback
import com.api.mocuti.entity.Participacao
import com.api.mocuti.entity.ParticipacaoId
import com.api.mocuti.repository.ParticipacaoRepository
import com.api.mocuti.service.ParticipacaoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.time.LocalDate

class ParticipacaoJpaControllerTest {

    private val repository = mock(ParticipacaoRepository::class.java)
    private val service = mock(ParticipacaoService::class.java)
    private val controller = ParticipacaoJpaController(repository, service)

    // Criação de ID composto
    val idParticipacao = ParticipacaoId(
        usuarioId = 1,
        eventoId = 1
    )

    // Criação do DTO real (sem mock)
    val dto = ParticipacaoFeedbackDTO(
        id = idParticipacao,
        isPresente = true,
        email = "usuario@teste.com",
        nomeEvento = "Workshop Kotlin",
        nota = NotaFeedback(5, "Excelente evento!"),
        comentario = "Muito bom!"
    )

    @Test
    fun `GET todas participacoes com dados deve retornar 200 e lista`() {
        val participacoes = listOf(mock(Participacao::class.java))
        `when`(repository.findAll()).thenReturn(participacoes)

        val resposta = controller.getParticipacao()

        assertEquals(200, resposta.statusCode.value())
        assertEquals(1, resposta.body?.size)
        verify(repository, times(1)).findAll()
    }

    @Test
    fun `GET todas participacoes sem dados deve retornar 204`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val resposta = controller.getParticipacao()

        assertEquals(204, resposta.statusCode.value())
        assertNull(resposta.body)
        verify(repository, times(1)).findAll()
    }

    @Test
    fun `listarParticipacoes deve retornar lista de DTOs`() {
        // Data que o controller vai usar
        val cincoDiasAtras = LocalDate.now().minusDays(5)

        val idParticipacao = ParticipacaoId(usuarioId = 1, eventoId = 1)
        val dto = ParticipacaoFeedbackDTO(
            id = idParticipacao,
            isPresente = true,
            email = "usuario@teste.com",
            nomeEvento = "Workshop Kotlin",
            nota = NotaFeedback(5, "Excelente evento!"),
            comentario = "Muito bom!"
        )

        val listaEsperada = listOf(dto)

        // Mock usando exatamente a mesma data
        `when`(service.listarParticipacoesFiltradasPorUsuario(1, cincoDiasAtras))
            .thenReturn(listaEsperada)

        val resposta = controller.listarParticipacoes(1)

        assertEquals(listaEsperada, resposta)
        verify(service, times(1)).listarParticipacoesFiltradasPorUsuario(1, cincoDiasAtras)
    }

    @Test
    fun `listarEventosPassados deve retornar lista de DTOs`() {
        val cincoDiasAtras = LocalDate.now().minusDays(5)

        val dto = ParticipacaoFeedbackDTO(
            id = ParticipacaoId(usuarioId = 1, eventoId = 1),
            isPresente = true,
            email = "usuario@teste.com",
            nomeEvento = "Workshop Kotlin",
            nota = NotaFeedback(5, "Excelente evento!"),
            comentario = "Muito bom!"
        )

        val listaEsperada = listOf(dto)

        `when`(service.listarParticipacoesPassadas(1, cincoDiasAtras))
            .thenReturn(listaEsperada)

        val resposta = controller.listarEventosPassados(1)

        assertEquals(listaEsperada, resposta)
        verify(service, times(1)).listarParticipacoesPassadas(1, cincoDiasAtras)
    }

    @Test
    fun `inscreverUsuario deve retornar 201`() {
        doNothing().`when`(service).inscreverUsuario(1, 2, 3)

        val resposta = controller.inscreverUsuario(1, 2, 3)

        assertEquals(201, resposta.statusCode.value())
        verify(service, times(1)).inscreverUsuario(1, 2, 3)
    }

    @Test
    fun `cancelarInscricao deve retornar 204`() {
        doNothing().`when`(service).cancelarInscricao(1, 2)

        val resposta = controller.cancelarInscricao(1, 2)

        assertEquals(204, resposta.statusCode.value())
        verify(service, times(1)).cancelarInscricao(1, 2)
    }

    @Test
    fun `listarEventosInscritos deve retornar 200 com lista de eventos`() {
        val eventos = listOf(mock(Evento::class.java))
        `when`(service.listarEventosInscritos(1)).thenReturn(eventos)

        val resposta = controller.listarEventosInscritos(1)

        assertEquals(200, resposta.statusCode.value())
        assertEquals(eventos, resposta.body)
        verify(service, times(1)).listarEventosInscritos(1)
    }

    @Test
    fun `listarEventosInscritos deve retornar 204 quando lista vazia`() {
        `when`(service.listarEventosInscritos(1)).thenReturn(emptyList())

        val resposta = controller.listarEventosInscritos(1)

        assertEquals(204, resposta.statusCode.value())
        assertNull(resposta.body)
        verify(service, times(1)).listarEventosInscritos(1)
    }
}