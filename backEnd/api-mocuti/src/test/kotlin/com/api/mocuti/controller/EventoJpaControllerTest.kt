package com.api.mocuti.controller

import com.api.mocuti.dto.*
import com.api.mocuti.entity.*
import com.api.mocuti.service.EventoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.assertContentEquals

class EventoJpaControllerTest {

    private val service = mock(EventoService::class.java)
    private val controller = EventoJpaController(service)

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
    fun `GET deve retornar 200 com lista de eventos`() {
        `when`(service.listarTodos()).thenReturn(listOf(evento))

        val response = controller.getEvento()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `GET deve retornar 204 se não houver eventos`() {
        `when`(service.listarTodos()).thenReturn(emptyList())

        val response = controller.getEvento()

        assertEquals(204, response.statusCode.value())
    }

    @Test
    fun `GET por ID deve retornar 200 se evento existir`() {
        `when`(service.buscarPorId(1)).thenReturn(evento)

        val response = controller.getPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `GET por ID deve retornar 404 se evento não existir`() {
        `when`(service.buscarPorId(1)).thenReturn(null)

        val response = controller.getPorId(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `POST deve retornar 201 com evento criado`() {
        val dto = mock(EventoCadastroRequest::class.java)
        `when`(service.criarEvento(dto)).thenReturn(evento)

        val response = controller.postEvento(dto)

        assertEquals(201, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `DELETE deve retornar 204 se evento for deletado`() {
        `when`(service.deletar(1)).thenReturn(true)

        val response = controller.deleteEvento(1)

        assertEquals(204, response.statusCode.value())
        verify(service, times(1)).deletar(1)
    }

    @Test
    fun `DELETE deve retornar 404 se evento não existir`() {
        `when`(service.deletar(1)).thenReturn(false)

        val response = controller.deleteEvento(1)

        assertEquals(404, response.statusCode.value())
        verify(service, times(1)).deletar(1)
    }

    @Test
    fun `PUT deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAtualizarRequest::class.java)
        `when`(service.atualizarEvento(1, dto)).thenReturn(evento)

        val response = controller.putEvento(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `PATCH dia e hora deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAttDiaHoraRequest::class.java)
        `when`(service.atualizarDiaHora(1, dto)).thenReturn(evento)

        val response = controller.patchDiaHora(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `GET foto deve retornar 200 se evento tiver imagem`() {
        val fotoBytes = byteArrayOf(1, 2, 3)

        // Usa doReturn para evitar NullPointerException em Kotlin
        doReturn(fotoBytes).`when`(service).getFoto(1)

        val response = controller.getFoto(1)

        // Verificações
        assertEquals(200, response.statusCode.value())
        assertContentEquals(fotoBytes, response.body) // versão idiomática de assertArrayEquals

        verify(service).getFoto(1) // garante que o service foi chamado
    }

    @Test
    fun `GET foto deve retornar 404 se evento não tiver imagem`() {
        `when`(service.getFoto(1)).thenReturn(null)

        val response = controller.getFoto(1)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `PATCH foto deve retornar 200 com foto atualizada`() {
        val mockFile = mock(MultipartFile::class.java)
        val bytes = byteArrayOf(5, 6, 7)
        `when`(mockFile.bytes).thenReturn(bytes)

        val fotoAtualizada = FotoRequest(bytes)
        `when`(service.atualizarFoto(1, bytes)).thenReturn(fotoAtualizada)

        val response = controller.patchFoto(1, mockFile)

        assertEquals(200, response.statusCode.value())
        assertArrayEquals(bytes, response.body?.foto)
    }

    @Test
    fun `PATCH status deve retornar 200 com evento atualizado`() {
        val dto = mock(EventoAtualizaStatusRequest::class.java)
        `when`(service.atualizarStatusEvento(1, dto)).thenReturn(evento)

        val response = controller.patchStatusEvento(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(evento, response.body)
    }

    @Test
    fun `GET eventos-usuario deve retornar 200 com lista`() {
        val lista = listOf(mock(EventosUsuariosRequest::class.java))
        `when`(service.getEventosUsuario()).thenReturn(lista)

        val response = controller.getEventoUsuario()

        assertEquals(200, response.statusCode.value())
        assertEquals(lista, response.body)
    }

    @Test
    fun `GET eventos-usuario por id deve retornar 200 com dados`() {
        val eventoUsuario = mock(EventosUsuariosRequest::class.java)
        `when`(service.getEventosUsuarioId(1)).thenReturn(eventoUsuario)

        val response = controller.getEventoUsuarioPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(eventoUsuario, response.body)
    }

    @Test
    fun `GET por-eventos deve retornar 200 com lista filtrada`() {
        val filtro = listOf(mock(EventoDTO::class.java))

        // Cria o mesmo filtro que o controller vai construir internamente
        val filtroRequest = EventoFiltroRequest(
            nome = null,
            dataInicio = null,
            dataFim = null,
            categoriaId = null,
            statusEventoId = null
        )

        // Mock exato da chamada
        `when`(service.buscarComFiltros(filtroRequest))
            .thenReturn(filtro)

        // Chama o método do controller
        val response = controller.listarEventos(null, null, null, null, null)

        // Verifica resultado
        assertEquals(1, response.size)
        verify(service, times(1)).buscarComFiltros(filtroRequest)
    }


    @Test
    fun `GET por-categoria deve retornar lista`() {
        val lista = listOf(mock(EventoDTO::class.java))
        `when`(service.buscarPorCategoria(1)).thenReturn(lista)

        val response = controller.listarEventosPorCategoria(1)

        assertEquals(1, response.size)
    }

    @Test
    fun `GET por-status deve retornar lista`() {
        val lista = listOf(mock(EventoDTO::class.java))
        `when`(service.buscarPorStatus(2)).thenReturn(lista)

        val response = controller.listarEventosPorStatus(2)

        assertEquals(1, response.size)
    }
}
