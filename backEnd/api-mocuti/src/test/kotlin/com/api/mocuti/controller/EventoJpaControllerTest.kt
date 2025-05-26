package com.api.mocuti.controller

import com.api.mocuti.entity.Categoria
import com.api.mocuti.entity.Endereco
import com.api.mocuti.entity.Evento
import com.api.mocuti.repository.EventoRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.sql.Time
import java.time.LocalDate
import java.util.*

class EventoJpaControllerTest {

    private val repository: EventoRepository = mock(EventoRepository::class.java)
    private val controller = EventoJpaController(repository)

    private lateinit var eventoTeste: Evento
    private lateinit var enderecoTeste: Endereco
    private lateinit var categoriaTeste: Categoria

    @BeforeEach
    fun setup() {
        enderecoTeste = Endereco(
            idEndereco = 1,
            CEP = "12345678",
            logradouro = "Rua das Flores",
            numero = 123,
            complemento = "Apto 45",
            UF = "SP",
            estado = "São Paulo",
            bairro = "Centro"
        )

        categoriaTeste = Categoria(
            id = 1,
            nome = "Tecnologia",
            descricao = "Eventos voltados para inovação e tecnologia"
        )

        eventoTeste = Evento(
            idEvento = 1,
            nomeEvento = "Feira de Tecnologia",
            descricao = "Evento com foco em inovação e startups",
            dia = LocalDate.of(2025, 6, 15),
            horaInicio = Time.valueOf("09:00:00"),
            horaFim = Time.valueOf("17:00:00"),
            isAberto = true,
            qtdVaga = 100,
            qtdInteressado = 25,
            foto = null, // ou ByteArray(0) se quiser evitar null
            endereco = enderecoTeste,
            statusEvento = 1,
            publicoAlvoEvento = 2,
            categoria = categoriaTeste
        )

        `when`(repository.findById(1)).thenReturn(Optional.of(eventoTeste))
        `when`(repository.existsById(1)).thenReturn(true)
        `when`(repository.existsById(999)).thenReturn(false)
    }

    // Testes de GET sem parametros
    @Test
    fun `A consulta de todos os eventos com dados deve retornar status 200 com a lista correta`() {
        `when`(repository.findAll()).thenReturn(mutableListOf(mock(Evento::class.java)))

        val retorno = controller.get()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }


    //    Testes de GRT pro Id
    @Test
    fun `A consulta de todos os eventos sem dados deve retornar status 204 com a lista vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retorno = controller.get()

        assertEquals(204, retorno.statusCode.value())
        assertNull(retorno.body)
    }

    @Test
    fun `A consulta de um evento com dados deve retornar status 200 com o evento correto`() {
        val retorno = controller.get(eventoTeste.idEvento)

        assertEquals(200, retorno.statusCode.value())
        assertEquals(eventoTeste, retorno.body)
    }

    @Test
    fun `A consulta de um evento com dados deve retornar status 404 com nenhum evento`() {
        `when`(repository.findById(2)).thenReturn(Optional.empty())

        val retorno = controller.get(2)

        assertEquals(404, retorno.statusCode.value())
        assertNull(retorno.body)
    }

    // Teste de POST
    @Test
    fun `A postagem de um evento deve retornar status 201 com e o evento postado`() {
        val evento = Evento(
            idEvento = 2,
            nomeEvento = "Hackathon 2025",
            descricao = "Maratona de programação",
            dia = LocalDate.of(2025, 9, 12),
            horaInicio = Time.valueOf("08:00:00"),
            horaFim = Time.valueOf("20:00:00"),
            isAberto = true,
            qtdVaga = 150,
            qtdInteressado = 20,
            foto = null,
            endereco = mock(Endereco::class.java),
            statusEvento = 1,
            publicoAlvoEvento = 2,
            categoria = mock(Categoria::class.java)
        )
        `when`(repository.save(any(Evento::class.java))).thenReturn(evento)

        val retorno = controller.post(evento)

        assertEquals(201, retorno.statusCode.value())
        assertEquals(evento, retorno.body)

        verify(repository, times(1)).save(evento)
    }

    // Deste teste de DELETE
    @Test
    fun `A exclusão por id que existe deve chamar a exclusão do banco e retornar status 204 com o corpo vazio`() {
        val retorno = controller.delete(1)

        assertEquals(204, retorno.statusCode.value())
        Assertions.assertNull(retorno.body)

        verify(repository, times(1)).deleteById(1)
    }

    @Test
    fun `A exclusão por código que NÃO existe NÃO deve chamar a exclusão do banco e retornar status 404 com o corpo vazio`() {
        val retorno = controller.delete(2)

        assertEquals(404, retorno.statusCode.value())
        Assertions.assertNull(retorno.body)
        verify(repository, times(0)).deleteById(1)
    }

    // Testes para PUT
    @Test
    fun `deve atualizar evento existente e retornar 200`() {
        val eventoAtualizado = eventoTeste.copy(nomeEvento = "Evento Atualizado")
        `when`(repository.save(any(Evento::class.java))).thenReturn(eventoAtualizado)

        val response = controller.put(1, eventoAtualizado)

        assertEquals(200, response.statusCode.value())
        assertEquals("Evento Atualizado", response.body?.nomeEvento)

        verify(repository).save(eventoAtualizado)
    }

    @Test
    fun `deve retornar 404 ao tentar atualizar evento inexistente`() {
        val response = controller.put(999, eventoTeste)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)

        verify(repository, never()).save(any(Evento::class.java))
    }

    // Teste para PATCH
    @Test
    fun `deve atualizar dia e hora quando evento existir`() {
        val id = 1
        val dia = LocalDate.of(2025, 10, 15)
        val horaInicio = Time.valueOf("10:00:00")
        val horaFim = Time.valueOf("18:00:00")

        // Simula existência do evento
        `when`(repository.existsById(id)).thenReturn(true)
        `when`(repository.findById(id)).thenReturn(Optional.of(eventoTeste))

        val response = controller.patchDiaHora(id, dia, horaInicio, horaFim)

        assertEquals(200, response.statusCode.value())
        assertEquals(eventoTeste, response.body)

        verify(repository).atualizarDiaHora(id, dia, horaInicio, horaFim)
        verify(repository).findById(id)
    }

    @Test
    fun `deve retornar 404 ao tentar atualizar dia e hora de evento inexistente`() {
        val dia = LocalDate.of(2025, 10, 15)
        val horaInicio = Time.valueOf("10:00:00")
        val horaFim = Time.valueOf("18:00:00")

        val response = controller.patchDiaHora(999, dia, horaInicio, horaFim)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)

        verify(repository, never()).atualizarDiaHora(
            eventoTeste.idEvento,
            dia,
            horaInicio,
            horaFim
        )
        verify(repository, never()).findById(any())
    }

    // Teste para PATCH de foto
    @Test
    fun `deve retornar 404 se foto de evento não existir`() {
        val foto = byteArrayOf(1, 2, 3)
        val response = controller.patchFoto(999, foto)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)

        verify(repository, never()).save(any())
    }

    @Test
    fun `deve atualizar foto e retornar evento com status 200`() {
        val foto = byteArrayOf(10, 20, 30)

        `when`(repository.save(any(Evento::class.java))).thenAnswer { it.arguments[0] }

        val response = controller.patchFoto(eventoTeste.idEvento, foto)

        assertEquals(200, response.statusCode.value())

        val eventoAtualizado = response.body

        assertNotNull(eventoAtualizado)
        assertArrayEquals(foto, eventoAtualizado.foto)

        verify(repository).existsById(eventoTeste.idEvento)
        verify(repository).findById(eventoTeste.idEvento)
        verify(repository).save(eventoAtualizado)
    }
}