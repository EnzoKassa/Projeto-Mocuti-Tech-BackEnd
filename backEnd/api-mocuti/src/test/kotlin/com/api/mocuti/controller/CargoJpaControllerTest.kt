package com.api.mocuti.controller

import com.api.mocuti.entity.Cargo
import com.api.mocuti.repository.CargoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CargoJpaControllerTest{
    val repository = mock(CargoRepository::class.java)

    val controller = CargoJpaController(repository)

    @Test
    fun `A consulta de todos os cargos com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(Cargo::class.java)))

        val retorno = controller.get()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }


    //    Testes de GET pro Id
    @Test
    fun `A consulta de todos os cargos sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.get()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }
}