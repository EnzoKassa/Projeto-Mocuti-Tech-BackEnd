package com.api.mocuti.controller

import com.api.mocuti.entity.Preferencia
import com.api.mocuti.repository.PreferenciaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PreferenciaJpaControllerTest {
    val repository = mock(PreferenciaRepository::class.java)

    val controller = PreferenciaJpaController(repository)

    @Test
    fun `A consulta de todos as preferencias com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(Preferencia::class.java)))

        val retorno = controller.getPreferencia()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }

    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos as preferencias sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.getPreferencia()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}