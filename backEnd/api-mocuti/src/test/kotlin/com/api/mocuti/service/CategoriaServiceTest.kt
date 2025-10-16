package com.api.mocuti.service

import com.api.mocuti.dto.RankCategoriaRequest
import com.api.mocuti.entity.Categoria
import com.api.mocuti.repository.CategoriaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class CategoriaServiceTest {

    private lateinit var categoriaRepository: CategoriaRepository
    private lateinit var categoriaService: CategoriaService

    private val categoria = Categoria(
        idCategoria = 1,
        nome = "Categoria Teste",
        descricao = "Descrição Teste"
    )

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        categoriaRepository = mock(CategoriaRepository::class.java)
        categoriaService = CategoriaService(categoriaRepository)
    }

    @Test
    fun `listarTodos deve retornar lista de categorias`() {
        val lista = listOf(categoria)
        `when`(categoriaRepository.findAll()).thenReturn(lista)

        val result = categoriaService.listarTodos()

        assertEquals(1, result.size)
        assertEquals(categoria.nome, result[0].nome)
    }

    @Test
    fun `buscarPorId deve retornar categoria quando existe`() {
        `when`(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria))

        val result = categoriaService.buscarPorId(1)

        assertNotNull(result)
        assertEquals(categoria.idCategoria, result?.idCategoria)
    }

    @Test
    fun `buscarPorId deve retornar null quando categoria nao existe`() {
        `when`(categoriaRepository.findById(2)).thenReturn(Optional.empty())

        val result = categoriaService.buscarPorId(2)

        assertNull(result)
    }

    @Test
    fun `deletar deve retornar true quando categoria existe`() {
        `when`(categoriaRepository.existsById(1)).thenReturn(true)
        doNothing().`when`(categoriaRepository).deleteById(1)

        val result = categoriaService.deletar(1)

        assertTrue(result)
        verify(categoriaRepository).deleteById(1)
    }

    @Test
    fun `deletar deve retornar false quando categoria nao existe`() {
        `when`(categoriaRepository.existsById(2)).thenReturn(false)

        val result = categoriaService.deletar(2)

        assertFalse(result)
        verify(categoriaRepository, never()).deleteById(anyInt())
    }

    @Test
    fun `salvar deve retornar categoria salva`() {
        `when`(categoriaRepository.save(categoria)).thenReturn(categoria)

        val result = categoriaService.salvar(categoria)

        assertEquals(categoria, result)
        verify(categoriaRepository).save(categoria)
    }

    @Test
    fun `atualizar deve salvar quando categoria existe`() {
        val categoriaAtualizada = Categoria(
            idCategoria = 0,
            nome = "Atualizada",
            descricao = "Descricao Atualizada"
        )

        `when`(categoriaRepository.existsById(1)).thenReturn(true)
        `when`(categoriaRepository.save(any(Categoria::class.java)))
            .thenAnswer { it.arguments[0] }

        val result = categoriaService.atualizar(1, categoriaAtualizada)

        assertNotNull(result)
        assertEquals(1, result?.idCategoria)
        assertEquals("Atualizada", result?.nome)
    }

    @Test
    fun `atualizar deve retornar null quando categoria nao existe`() {
        val categoriaAtualizada = Categoria(0, "Atualizada", "Descricao Atualizada")
        `when`(categoriaRepository.existsById(2)).thenReturn(false)

        val result = categoriaService.atualizar(2, categoriaAtualizada)

        assertNull(result)
    }

    @Test
    fun `getRanking deve retornar lista de RankCategoriaRequest`() {
        // Criando uma implementação anônima da interface
        val ranking = listOf(object : RankCategoriaRequest {
            override val id_categoria: Int = 1
            override val nome: String = "Categoria Teste"
            override val descricao: String = "Descrição Teste"
            override val total_votos: Int = 10
        })

        `when`(categoriaRepository.buscarRanking()).thenReturn(ranking)

        val result = categoriaService.getRanking()

        assertEquals(1, result.size)
        assertEquals("Categoria Teste", result[0].nome)
        assertEquals(10, result[0].total_votos)
    }

}
