package com.api.mocuti.controller

import com.api.mocuti.entity.PublicoAlvo
import com.api.mocuti.repository.PublicoAlvoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PublicoAlvoJpaControllerTest {
    val repository = mock(PublicoAlvoRepository::class.java)

    val controller = PublicoAlvoJpaController(repository)

    @Test
    fun `A consulta de todos os publicos alvos com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(PublicoAlvo::class.java)))

        val retorno = controller.get()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }


    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos os publicos alvos sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.get()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}