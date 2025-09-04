package com.api.mocuti.controller

import com.api.mocuti.entity.NotaFeedback
import com.api.mocuti.repository.NotaFeedbackRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class NotaFeedbackJpaControllerTest {
    val repository = mock(NotaFeedbackRepository::class.java)

    val controller = NotaFeedbackJpaController(repository)

    @Test
    fun `A consulta de todos as notas feedback com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(NotaFeedback::class.java)))

        val retorno = controller.getNota()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }


    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos as notas feedback sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.getNota()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}