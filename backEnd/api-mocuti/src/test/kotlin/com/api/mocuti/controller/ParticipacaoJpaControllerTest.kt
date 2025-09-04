package com.api.mocuti.controller

import com.api.mocuti.entity.Participacao
import com.api.mocuti.repository.ParticipacaoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ParticipacaoJpaControllerTest {
    val repository = mock(ParticipacaoRepository::class.java)

    val controller = ParticipacaoJpaController(repository)

    @Test
    fun `A consulta de todos as participacoes com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(Participacao::class.java)))

        val retorno = controller.getParticipacao()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }


    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos as participacoes sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.getParticipacao()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}