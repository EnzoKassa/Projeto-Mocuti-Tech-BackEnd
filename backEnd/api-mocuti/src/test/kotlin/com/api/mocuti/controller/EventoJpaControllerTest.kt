package com.api.mocuti.controller

import EventoService
import com.api.mocuti.dto.*
import com.api.mocuti.entity.*
import com.api.mocuti.repository.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class EventoJpaControllerTest {

    private val eventoRepo = mock(EventoRepository::class.java)
    private val enderecoRepo = mock(EnderecoRepository::class.java)
    private val statusRepo = mock(StatusEventoRepository::class.java)
    private val categoriaRepo = mock(CategoriaRepository::class.java)
    private val service = mock(EventoService::class.java)

    private val controller = EventoJpaController(eventoRepo, enderecoRepo, statusRepo, categoriaRepo, service)

    private lateinit var evento: Evento

    @BeforeEach
    fun setup() {
        val endereco = Endereco(1, "12345678", "Rua", 123, "Apt", "SP", "São Paulo", "Centro")
        val status = StatusEvento(1, "Ativo")
        val categoria = Categoria(1, "Tech", "Evento de tech")

        evento = Evento(
            idEvento = 1,
            nomeEvento = "Nome",
            descricao = "Descrição",
            dia = LocalDate.now(),
            horaInicio = LocalTime.of(10, 0),
            horaFim = LocalTime.of(12, 0),
            isAberto = true,
            qtdVaga = 100,
            qtdInteressado = 10,
            publicoAlvo = "Todos",
            endereco = endereco,
            statusEvento = status,
            categoria = categoria
        )
    }

    @Test
    fun `get deve retornar 200 com lista de eventos`() {
        `when`(eventoRepo.findAll()).thenReturn(listOf(evento))

        val response = controller.get()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `get deve retornar 204 se não houver eventos`() {
        `when`(eventoRepo.findAll()).thenReturn(emptyList())

        val response = controller.get()

        assertEquals(204, response.statusCode.value())
    }

    @Test
    fun `getPorId deve retornar 200 se evento existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(true)
        `when`(eventoRepo.findById(1)).thenReturn(Optional.of(evento))

        val response = controller.getPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `getPorId deve retornar 404 se evento não existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(false)

        val response = controller.getPorId(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `post deve retornar 201 com evento criado`() {
        val dto = mock(EventoCadastroRequest::class.java)

        `when`(service.criarEvento(dto)).thenReturn(evento)

        val response = controller.post(dto)

        assertEquals(201, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `delete deve retornar 204 se evento existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(true)

        val response = controller.delete(1)

        assertEquals(204, response.statusCode.value())
        verify(eventoRepo).deleteById(1)
    }

    @Test
    fun `delete deve retornar 404 se evento não existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(false)

        val response = controller.delete(1)

        assertEquals(404, response.statusCode.value())
        verify(eventoRepo, never()).deleteById(1)
    }

    @Test
    fun `put deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAtualizarRequest::class.java)
        `when`(service.atualizarEvento(1, dto)).thenReturn(evento)

        val response = controller.put(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `patchDiaHora deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAttDiaHoraRequest::class.java)
        `when`(service.atualizarDiaHora(1, dto)).thenReturn(evento)

        val response = controller.patchDiaHora(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `getFoto deve retornar 200 com imagem se existir`() {
        val fotoBytes = byteArrayOf(1, 2, 3)
        evento.foto = fotoBytes

        `when`(eventoRepo.existsById(1)).thenReturn(true)
        `when`(eventoRepo.findById(1)).thenReturn(Optional.of(evento))

        val response = controller.getFoto(1)

        assertEquals(200, response.statusCode.value())
        assertArrayEquals(fotoBytes, response.body)
    }

    @Test
    fun `getFoto deve retornar 404 se evento não existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(false)

        val response = controller.getFoto(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `patchFoto deve retornar 200 com evento atualizado`() {
        val novaFoto = byteArrayOf(4, 5, 6)
        evento.foto = novaFoto

        `when`(eventoRepo.existsById(1)).thenReturn(true)
        `when`(eventoRepo.findById(1)).thenReturn(Optional.of(evento))
        `when`(eventoRepo.save(any())).thenReturn(evento)

        val response = controller.patchFoto(1, novaFoto)

        assertEquals(200, response.statusCode.value())
        assertArrayEquals(novaFoto, response.body?.foto)
    }

    @Test
    fun `patchFoto deve retornar 404 se evento não existir`() {
        `when`(eventoRepo.existsById(1)).thenReturn(false)

        val response = controller.patchFoto(1, byteArrayOf(1, 2, 3))

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `patchStatusEvento deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAtualizaStatusRequest::class.java)
        `when`(service.atualizarStatusEvento(1, dto)).thenReturn(evento)

        val response = controller.patchStatusEvento(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }
}
