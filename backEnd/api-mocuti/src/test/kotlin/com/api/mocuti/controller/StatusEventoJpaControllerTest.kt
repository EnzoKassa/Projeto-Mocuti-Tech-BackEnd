package com.api.mocuti.controller

import com.api.mocuti.entity.StatusEvento
import com.api.mocuti.repository.StatusEventoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class StatusEventoJpaControllerTest {
    val repository = mock(StatusEventoRepository::class.java)

    val controller = StatusEventoJpaController(repository)

    @Test
    fun `A consulta de todos os status de eventos com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(StatusEvento::class.java)))

        val retorno = controller.getStatus()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }

    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos os status de eventos sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.getStatus()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}