package com.api.mocuti.controller

import com.api.mocuti.entity.CanalComunicacao
import com.api.mocuti.repository.CanalComunicacaoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CanalComunicacaoJpaControllerTest {
    val repository = mock(CanalComunicacaoRepository::class.java)

    val controller = CanalComunicacaoJpaController(repository)

    @Test
    fun `A consulta de todos os canais com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(CanalComunicacao::class.java)))

        val retorno = controller.getCanalComunicacao()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }

    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos os canais sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.getCanalComunicacao()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}